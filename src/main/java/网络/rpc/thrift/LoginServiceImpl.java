package 网络.rpc.thrift;


import org.apache.thrift.TException;



public class LoginServiceImpl implements LoginService.Iface{

    @Override
    public String doAction(Request request) throws RequestException,TException {
        // TODO Auto-generated method stub
        System.out.println("hahaha");
        System.out.println("username:"+request.getUsername());
        System.out.println("password:"+request.getPassword());
        return request.getUsername()+request.getPassword();
    }

}
