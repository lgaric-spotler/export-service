package com.spotler.core.apiclients;

import com.spotler.api.mailing.Mailing;
import com.spotler.core.model.account.Account;
import org.glassfish.jersey.client.JerseyClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class MailPlusClient {
    protected final JerseyClient client;

    @Inject
    public MailPlusClient(@Named("mailPlus") JerseyClient client) {
        this.client = client;
    }

    public List<Mailing> getMailings(Account account, LocalDateTime isoFromDate, LocalDateTime isoToDate) {
        return getWebTargetRest(account, "mailing")
                .queryParam("fromDate", isoFromDate.atZone(ZoneId.systemDefault()))
                .queryParam("toDate", isoToDate.atZone(ZoneId.systemDefault()))
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    private WebTarget getWebTargetRest(Account account, String resource) {
        return client.target(String.format("https://%s/genericservice/rest/%d/%s", account.getServer(),
                account.getId(), resource));
    }
}
