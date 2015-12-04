import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wujie
 * one OS Page Replacement Algorithm's implementation 
 * Aging Algorithm, which  is similarly LRU Algorithm
 */
public class AgingAlgorithm {
	
	public  List<Page> pageLists = new ArrayList<Page>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AgingAlgorithm agingAlgorithm = new AgingAlgorithm();
		agingAlgorithm.init();
		agingAlgorithm.simulation();
		
	}
	
	public void init() {
		//init 5 page items
		for(int i = 0; i < 5; i++) {
			pageLists.add(new Page(i));
		}
	}
	
	public void accessPageItem(int index) {
		try {
			Thread.currentThread().sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pageLists.get(index).setR(true);	
	}
	
	/**
	 *
	 * @return the weed out page
	 */
	public Page getWeedOutPage() {
		Page target = pageLists.get(0);
		for(int i = 0; i < 5; i++) {
			if(target.getCounter() > pageLists.get(i).getCounter())
				target = pageLists.get(i);
		}
		return target;
	}
	
	/**
	 * simulation a page in and out
	 */
	public void simulation() {
		//clock period
		Thread clockThread = new ClockThread(pageLists){
			@Override
			public void run() {
				while(true){
					try {
						Thread.currentThread().sleep(50);
						for(int i = 0; i < 5; i++)
							pageList.get(i).modifyCounter();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		};
		clockThread.start();
		
		//simulation access a page
		int[] indexs = {0,0,3,0,1,0,4,2,3,1,
						0,0,4,0,3,2,2,0,3,4,
						0,3,0,3,1,0,2,3,0,1,
						4,0,0,0,1,0,2,3,0,3,
						0,0,3,0,1,0,2,0,0,0,
		};
		for(int i = 0; i < indexs.length; i++) {
			accessPageItem(indexs[i]);
		}
		
		//page fault
		Page weedOutPage = getWeedOutPage();
		System.out.println("The weed out page is " + weedOutPage + "");
		for(int i = 0; i < 5; i++)
			System.err.println(pageLists.get(i) + " the counter is " + pageLists.get(i).getCounter());
		
		clockThread.yield();
		
	}
}

/**
 * Virtual Memory's Page Item
 * @author wujie
 *
 */
class Page {
	/**
	 * here we will cost 7 bits(the first bit is 0 to ensure counter is positive) 
	 * as a counter, Every clock period, we firstly 
	 */
	private int pageNo;
	private byte counter = 0;
	
	/**
	 * reference bit at Page Item
	 */
	private boolean r = false;
	
	public Page(int pageNo){
		this.pageNo = pageNo;
	}
	
	public byte getCounter(){
		return this.counter;
	}
	
	public void modifyCounter() {
		this.counter = (byte) (this.counter >> 1);
		if(getR())
			this.counter = (byte) (this.counter | 64);//010000000 is 64
		else
			this.counter = (byte) (this.counter | 0);
		setR(false);
	}
	
	public synchronized void setR(boolean r) {
		this.r = r;
	}
	
	public synchronized boolean getR() {
		return this.r;
	}
	
	@Override
	public String toString(){
		return "The pageNo is " + this.pageNo;
	}
}

class ClockThread extends Thread {
	List<Page> pageList;
	public ClockThread (List<Page> pageList) {
		this.pageList = pageList;
	}
}


