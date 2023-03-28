package org.example;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkExecutor extends RecursiveTask<String> {

    private String url;
    private static String startUrl;
    private static CopyOnWriteArraySet<String> allLinks = new CopyOnWriteArraySet<>();

    public LinkExecutor(String url){
        this.url = url.trim();
    }

    public LinkExecutor(String url, String startUrl){
        this.url = url.trim();
        LinkExecutor.startUrl = startUrl.trim();
    }

    @Override
    protected String compute() {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<LinkExecutor> subTask = new HashSet<>();
        getChildren(subTask);
        for (LinkExecutor linkExecutor : subTask){
            stringBuffer.append(linkExecutor.join());
        }
        return stringBuffer.toString();
    }

    private void getChildren(Set<LinkExecutor> subTask) {
        Document document;
        Elements elements;
        try {
            Thread.sleep(150);
            document = Jsoup.connect(url).get();
            elements = document.select("a");
            for (Element elements1 : elements){
                String attr = elements1.attr("abs:href");
                if (!attr.isEmpty() && attr.startsWith(startUrl) && !allLinks.contains(attr) && !attr.contains("#")){
                    LinkExecutor linkExecutor = new LinkExecutor(attr);
                    linkExecutor.fork();
                    subTask.add(linkExecutor);
                    allLinks.add(attr);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
