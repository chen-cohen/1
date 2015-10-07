package com.bizzabo.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {

	private static Crawler instance = new Crawler();
	private static Set<String> pagesVisited = new HashSet<String>();
		public static final List<String> pagesToVisitList = new LinkedList<String>();
//	public final static BlockingQueue<String> pagesToVisitList = new LinkedBlockingQueue<String>();
	private String basePageUrl;
	private int numberOfWorkers;
	private static int totalPagesToScan;
	public static final Object lock = new Object();


	private Crawler()
	{

	}


	public static Crawler getInstance()
	{
		if (instance == null)
		{
			synchronized(Crawler.class) {
				Crawler inst = instance;
				if (inst == null){
					synchronized(Crawler.class) {
						instance = new Crawler();
					}
				}
			}
		}
		return instance;
	}

	public static synchronized String nextUrl() throws InterruptedException
	{
		String link = pagesToVisitList.remove(0);
		if (pagesVisited.contains(link))
		{
			return null;
		}
		else{
			pagesVisited.add(link);
			return 	link;
		}
	}

	public void getInput() throws Exception
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Enter A Base Page Url");
		this.basePageUrl = in.nextLine();

		if (!this.basePageUrl.startsWith("http://")){
			throw new Exception("An Url Must Start With:  http:// format");
		}

		System.out.println("Enter Number Of Workers");
		this.numberOfWorkers = in.nextInt();

		System.out.println("Enter A Number Of Total Pages To Scan");
		totalPagesToScan = in.nextInt();
	}

	protected void crawl() throws InterruptedException
	{
		pagesToVisitList.add(basePageUrl);
		ExecutorService executor = Executors.newFixedThreadPool(this.numberOfWorkers);
		for(String s : pagesToVisitList)
		{
			decTotalPagesToScan();
			Runnable worker = new WorkerThread(nextUrl());
			executor.execute(worker);
				synchronized (lock)
				{
					worker.wait();
				}

		}
		



//		executor.shutdown();
//		while (!executor.isTerminated()) {
//		}
//		System.out.println("Finished all threads");


//		while ( totalPagesToScan >0 && !pagesToVisitList.isEmpty()) {
//			executor.submit(new Runnable() {
//
//
//				public void run() {
//					while (!Thread.currentThread().isInterrupted() ) {
//						try {
//							System.out.println(Thread.currentThread().getName());
//							String item = pagesToVisitList.take();
//							try
//							{
//								if (totalPagesToScan >0)
//								{
//									jsoup(item);
//									Crawler.decTotalPagesToScan();
//								}
//								else{
//									throw new InterruptedException("ss");
//								}
//							} catch(IOException e)
//							{
//								e.printStackTrace();
//							}
//
//							// Process item
//						} catch (InterruptedException ex) {
//							Thread.currentThread().interrupt();
//							break;
//						}
//					}
//				}
//			});
//		}


	}

	public static synchronized void decTotalPagesToScan()
	{
		totalPagesToScan--;
	}

	public static  void addPageToPagesToVisitList(String link)
	{
		pagesToVisitList.add(link);
//		Crawler.pagesToVisitList.notifyAll();
		synchronized (lock) {
			lock.notifyAll();
		}
	}


	public void jsoup (String link) throws IOException, InterruptedException
	{

		Connection connection = Jsoup.connect(link);
		Document htmlDocument = connection.get();
		Elements linksOnPage = htmlDocument.select("a[href]");
		System.out.println(htmlDocument.select("title").text());
		pagesToVisitList.add("http://ifatzohar.com");
	}
}

