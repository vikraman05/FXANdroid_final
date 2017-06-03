package comin.chaten.statens.caugfitten.servernendto.changesdw;

import org.fejoa.library.Client;
import org.fejoa.library.command.ContactRequestCommand;
import org.fejoa.library.command.ContactRequestCommandHandler;
import org.fejoa.library.remote.TaskUpdate;
import org.fejoa.library.support.Task;

import comin.chaten.statens.caugfitten.errorfinder.Writer;


public class AddDetails implements Runnable {

    private final Client client;
    private final Userdetails userdetails;

    public AddDetails(Client client, Userdetails userdetails) {
        this.client = client;
        this.userdetails = userdetails;
    }

    @Override
    public void run() {
        try {
            syncAccount(client, userdetails);
        } catch (Exception e) {
            Writer.error(e.getMessage());
        }
    }

    private void syncAccount(final Client client, final Userdetails status) throws Exception {
        client.startSyncing(new SyncObserver(status));
    }

    private class SyncObserver implements Task.IObserver<TaskUpdate, Void> {

        private Userdetails status;

        public SyncObserver(Userdetails status) {
            this.status = status;
        }

        @Override
        public void onProgress(TaskUpdate update) {
            Writer.debug(update.toString());
            if (!status.firstSync && update.getTotalWork() > 0 && update.getProgress() == update.getTotalWork()) {
                status.firstSync = true;
                try {
                    client.startCommandManagers(new Task.IObserver<TaskUpdate, Void>() {

                        @Override
                        public void onProgress(TaskUpdate update) {
                            Writer.debug(status.name + ": " + update.toString());
                        }

                        @Override
                        public void onResult(Void aVoid) {
                            Writer.info(status.name + ": Command sent");
                        }

                        @Override
                        public void onException(Exception exception) {
                            Writer.error(exception.getMessage());
                        }
                    });

                    ContactRequestCommandHandler handler = (ContactRequestCommandHandler) client.getIncomingCommandManager()
                            .getHandler(ContactRequestCommand.COMMAND_NAME);
                    handler.setListener(new ContactRequestCommandHandler.AutoAccept() {
                        @Override
                        public void onError(Exception exception) {
                            Writer.error(exception.toString());
                        }
                    });
                } catch (Exception e) {
                    onException(e);
                }
            }
        }

        @Override
        public void onResult(Void aVoid) {
            Writer.debug(status.name + ": sync ok");
        }

        @Override
        public void onException(Exception exception) {
            Writer.error(exception.getMessage());
        }
    }
}
