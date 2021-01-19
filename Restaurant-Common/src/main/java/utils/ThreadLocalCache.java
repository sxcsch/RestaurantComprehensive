package utils;

/**
 * @Description:
 * @Author: CaoPenghui
 * @Date: 2020/9/28 12:06
 */
public class ThreadLocalCache {

    private static ThreadLocal<String> token = new ThreadLocal<>();

    private static ThreadLocal<String> requestId = new ThreadLocal<>();

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String currentToken) {
        token.set(currentToken);
    }


    public static String getRequestId() {
        return requestId.get();
    }

    public static void setRequestId(String currentRequestId) {
        requestId.set(currentRequestId);
    }


}
