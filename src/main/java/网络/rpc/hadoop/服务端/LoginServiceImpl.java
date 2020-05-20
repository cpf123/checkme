package 网络.rpc.hadoop.服务端;

/**
 * 具体实现类
 *
 */
public class LoginServiceImpl implements LoginServiceInterface {

    @Override
    public String login(String account, String pwd) {
        return account + " logged in " + ("123456".equals(pwd) ? "Successfully!" : "Failed!");
    }

}

