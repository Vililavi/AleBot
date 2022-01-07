import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException {
        String token = "TOKEN HERE";
        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new Main());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getMessage().getContentRaw().equals("!ale/show")) {
            event.getChannel().sendMessage("Type '!ale/help' for sales").queue();
        }

        if (event.getMessage().getContentRaw().equals("!ale/help")) {
            event.getChannel().sendMessage("Commands:" +
                    "\n\t!ale/show" +
                    "\n\t!ale/lidl").queue();
        }

        //TODO Pakkauskoot, alkuperäinen hinta ja alennusprosentti
        if (event.getMessage().getContentRaw().equals("!ale/lidl")) {
            event.getChannel().sendMessage("Lidlin alennukset:").queue();

            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.lidl.fi/tarjoukset").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;
            Elements titles = doc.select("h3.product__title");
            Elements sale_price = doc.select("span.pricebox__price");

            for (int i = 0; i < titles.size(); i++) {
                event.getChannel().sendMessage(titles.get(i).text()
                        + "  // Hinta " + sale_price.get(i).text()
                        + "\u20ac").queue();
            }
        }
    }
}

