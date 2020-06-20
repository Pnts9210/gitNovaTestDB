import okhttp3.*;

import java.io.IOException;
import java.util.Collections;

public class MailgunEmailClient implements EmailClient {

    private String apikey;

    public MailgunEmailClient(String apikey) {
        this.apikey = apikey;
    }

    @Override
    public void sendWelcomeEmail(String emailAddress) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String auth = Credentials.basic("api", apikey);
        HttpUrl mailgunURL = new HttpUrl.Builder()
                .scheme("https")
                .host("api.mailgun.net")
                .addPathSegments("v3/sandbox511544acce7b423d9ac56a85d869211d.mailgun.org/messages")
                .addQueryParameter("from", "Pontus <welcome@novacredit.se>")
                .addQueryParameter("to", emailAddress)
                .addQueryParameter("subject", "hello")
                .addQueryParameter("text", "testing")
                .build();
        System.out.println(mailgunURL.toString());

        Request request = new Request.Builder()
                .url(mailgunURL)
                .post(new FormBody(Collections.emptyList(), Collections.emptyList()))
                .addHeader("authorization", auth)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("OKHTTP: " + response.body().string());

    }

    @Override
    public int sendInvoice(Invoice invoice) throws IOException {

        String email = invoice.getEmail();
        email = "p.norderheim@gmail.com";
        String text = "Hej " + invoice.getFirtsName() + " " + invoice.getLastaName() + "\nDu har en försenad faktura på " + invoice.getLatePayAmount() + "kr. Faktura id " + invoice.getPreviousInvoice();

        OkHttpClient client = new OkHttpClient();
        String auth = Credentials.basic("api", apikey);
        HttpUrl mailgunURL = new HttpUrl.Builder()
                .scheme("https")
                .host("api.mailgun.net")
                .addPathSegments("v3/sandbox511544acce7b423d9ac56a85d869211d.mailgun.org/messages")
                .addQueryParameter("from", "Pontus <welcome@novacredit.se>")
                .addQueryParameter("to", email)
                .addQueryParameter("subject", "Overdue payment")
                .addQueryParameter("text", text)
                .build();
        System.out.println(mailgunURL.toString());

        Request request = new Request.Builder()
                .url(mailgunURL)
                .post(new FormBody(Collections.emptyList(), Collections.emptyList()))
                .addHeader("authorization", auth)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println("OKHTTP: " + response.body().string());

        // allt under 400 är ok, allt över 400 - 500 är mitt fel, allt över 500 servernsfel.
        return response.code();
    }

}
