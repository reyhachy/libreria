package com.totalplay.background;

/**
 * Created by jorgehdezvilla on 29/08/17.
 * FFM
 */

public interface WSCallback<R extends WSBaseResponseInterface> {

    void onRequestWS(String requestUrl);

    void onSuccessLoadResponse(String requestUrl, R baseResponse);

    void onErrorLoadResponse(String requestUrl, String messageError);

    void onErrorConnection();

}
