package 网络.rpc.hadoop.客户端;

/**
 * 登录接口
 *
 * 客服端与服务端versionID必须相同
 * 客服端与服务端该接口的包名必须相同
 */
public interface LoginServiceInterface {

    public static final long versionID = 1L;

    public String login(String account, String pwd);

}

