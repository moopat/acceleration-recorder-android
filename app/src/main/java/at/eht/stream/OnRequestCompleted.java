package at.eht.stream;

/**
 * @author Markus Deutsch
 */
public interface OnRequestCompleted {

    void onSuccess(Response response);
    void onError(Response response);
}
