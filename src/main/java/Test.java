import java.io.IOException;
import java.sql.SQLException;
//import java.net.http.HttpClient;


public class Test {

    //**********JONAS KOD************
/*
    private class Response {
        int userId;
        int id;
        String title;
        String body;
    }
 */


    private class User {
        int id;
        String name;
        String username;
        String email;
        Adress address;

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", Adress=" + address +
                    '}';
        }
    }

    private class Adress {
        String street;
        String city;
        String zipcode;

        @Override
        public String toString() {
            return "Adress{" +
                    "street='" + street + '\'' +
                    ", city='" + city + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) throws IOException, SQLException, NullPointerException {


        Init init = new Init();
        Metoder metoder = new Metoder();
        //Invoice invoice = new Invoice();

        init.createSchema(true, 20);
       // System.out.println(metoder.getOverdueCustomerId());


        //System.out.println(metoder.createCustomersObjects().toString());

        System.out.println(metoder.makeLateInvoice());











/*
        //*****************JONAS KOD********************
        Gson gson = new Gson();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("https://jsonplaceholder.typicode.com/posts/1");
        CloseableHttpResponse execute = defaultHttpClient.execute(httpGet);
        byte[] bytes = execute.getEntity().getContent().readAllBytes();
        String s = new String(bytes);

        Response response = gson.fromJson(s, Response.class);
        System.out.println(response.body);

*/
        //****************GET*********************
   /*     DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://jsonplaceholder.typicode.com/users/1/posts");
        HttpResponse response = client.execute(request);

        // Get the response
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));

        String line = "";
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            //textView.append(line);
        }
*/

    /*
        //**************FUNKAR DEN??****************
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://jsonplaceholder.typicode.com/users");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            //nameValuePairs.add(new BasicNameValuePair("id", "111"));
            nameValuePairs.add(new BasicNameValuePair("title", "TestName!!"));
            nameValuePairs.add(new BasicNameValuePair("body", "TestBody!!"));
            nameValuePairs.add(new BasicNameValuePair("userId", "222"));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

     */

/*
        //******************GET WITH GSON*****************************
        Gson gson = new Gson();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://jsonplaceholder.typicode.com/users/1");
        HttpResponse response = client.execute(request);

        // Get the response
        BufferedReader rd = new BufferedReader
                (new InputStreamReader(
                        response.getEntity().getContent()));

        String line = "";
        String jsonObj = "";
        while ((line = rd.readLine()) != null) {
            jsonObj += line;
            System.out.println(line);
            //textView.append(line);
        }
        System.out.println("jsonString: " + jsonObj);
        User user = gson.fromJson(jsonObj, User.class);
        System.out.println("The object: " + user.toString());
*/
/*
        //****************** OKHTTP *****************
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/users/1/posts")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("OKHTTP: " + response.body().string());
*/
       /*
        //************* Faker exempel ************************
        Faker faker = new Faker();

        String name = faker.name().fullName(); // Miss Samanta Schmidt
        String firstName = faker.name().firstName(); // Emory
        String lastName = faker.name().lastName(); // Barton

        String streetAddress = faker.address().streetAddress();

        System.out.println(name );

        */


    }

}
