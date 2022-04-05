package 框架;



public class BaseClass {

    private String baseId;

    public BaseClass(String baseId) {
        super();
        this.baseId = baseId;
    }

    @Override
    public String toString() {
        return "BaseClass [baseId=" + baseId + "]";
    }

}