package factory;


public class RequestFactory {
    /**
     * Factory che a runtime istanzia la classe opportuna e la restituisce
     */

    public Request createRequest(String type) throws Exception {
        switch (type){
            case "C":
                return new CRequest();
            case "M":
                return new MRequest();
            case "D":
                return new DRequest();
            default:
                throw new Exception("Invalid type : " + type);
        }
    }
}
