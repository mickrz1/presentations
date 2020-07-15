import org.testng.annotations.DataProvider;

public class DataProviderClass {

    @DataProvider(name = "data-provider")
    public static Object[][] dataProviderMethod(){
        return new Object[][]{{"5"}, {"11763"}, {"42540aec-367a-4e5e-b411-17c09b08e41f"}, {"4bd97d96-f0ff-46cb-a52c-2992bd972bb1"}, {"a408e75a-cede-4587-8526-54e9be600d9f"}, {"38d588fd-7e9c-4c42-a4ae-6831775eca45"}, {"ea5b98dd-4b6f-4bd0-8c80-22c2629132d0"}, {"3"}, {"20782"}, {"3919"}, {"258832"}, {"1429"}, {"121882"}};
    }

}
