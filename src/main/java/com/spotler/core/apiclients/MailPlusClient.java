package com.spotler.core.apiclients;

import com.spotler.core.model.account.Account;
import com.spotler.model.mailplus.Mailing;
import org.glassfish.jersey.client.JerseyClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MailPlusClient {
    private static final DateTimeFormatter MP_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    protected final JerseyClient client;

    @Inject
    public MailPlusClient(@Named("mailPlus") JerseyClient client) {
        this.client = client;
    }

    public List<Mailing> getMailingStats(Account account, LocalDateTime isoFromDate, LocalDateTime isoToDate) {
        return getWebTargetRest(account, "mailing")
                .path("stats")
                .queryParam("fromDate", convertDateTime(isoFromDate))
                .queryParam("toDate", convertDateTime(isoToDate))
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    private WebTarget getWebTargetRest(Account account, String resource) {
        return client.target(String.format("https://%s/genericservice/rest/%d/%s", account.getServer(),
                account.getId(), resource));
    }

    private String convertDateTime(LocalDateTime localDateTime) {
        return MP_DATE_TIME_FORMAT.format(localDateTime.atZone(ZoneId.systemDefault()));
    }
}
