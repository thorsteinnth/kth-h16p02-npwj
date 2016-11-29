package load;

@SuppressWarnings("serial")
class LoadException extends Exception {
    public LoadException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}
