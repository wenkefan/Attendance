package hylk.com.xiaochekaoqin.global;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理器, 生成线程池
 * @author wzz
 * @date 2016-8-23
 */
public class ThreadManager {

	private static ThreadPool sInstance;

	public static ThreadPool getTheadPool() {
		if (sInstance == null) {
			synchronized (ThreadManager.class) {
				if (sInstance == null) {
					int cpuCount = Runtime.getRuntime().availableProcessors();
					System.out.println("cpu:" + cpuCount);
					int size = cpuCount * 2 + 1;//根据cpu个数推出线程数量
					sInstance = new ThreadPool(10, 10, 0L);
				}
			}
		}

		return sInstance;
	}

	public static class ThreadPool {

		private int corePoolSize;//核心线程数, 线程池中默认允许多少个线程同时运行
		private int maximumPoolSize;//最大线程数, 线程池最大允许多少个线程同时运行
		private long keepAliveTime;//允许线程休息的时间

		private ThreadPoolExecutor mExecutor;

		private ThreadPool(int corePoolSize, int maximumPoolSize,
				long keepAliveTime) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.keepAliveTime = keepAliveTime;
		}

		// 线程池几个参数的理解:
		// 比如去火车站买票, 有10个售票窗口, 但只有5个窗口对外开放. 那么对外开放的5个窗口称为核心线程数,
		// 而最大线程数是10个窗口.
		// 如果5个窗口都被占用, 那么后来的人就必须在后面排队, 但后来售票厅人越来越多, 已经人满为患, 就类似于线程队列已满.
		// 这时候火车站站长下令, 把剩下的5个窗口也打开, 也就是目前已经有10个窗口同时运行. 后来又来了一批人,
		// 10个窗口也处理不过来了, 而且售票厅人已经满了, 这时候站长就下令封锁入口,不允许其他人再进来, 这就是线程异常处理策略.
		// 而线程存活时间指的是, 允许售票员休息的最长时间, 以此限制售票员偷懒的行为.
		public void execute(Runnable r) {
			if (mExecutor == null) {
				mExecutor = new ThreadPoolExecutor(corePoolSize,// 核心线程数
						maximumPoolSize, // 最大线程数
						keepAliveTime, // 闲置线程存活时间
						TimeUnit.MILLISECONDS,// 时间单位
						new LinkedBlockingDeque<Runnable>(),// 线程队列, 此队列无界, 无限大
						Executors.defaultThreadFactory(),// 线程工厂
						new AbortPolicy()// 队列已满,而且当前线程数已经超过最大线程数时的异常处理策略
				);
			}

			//开始执行一个线程
			mExecutor.execute(r);//不一定立即执行, 取决于当前线程池中的数量
		}

		//取消异步任务
		public void cancel(Runnable r) {
			if (mExecutor != null) {
				//从线程队列中移除当前的异步任务
				//局限性: 如果任务已经跑起来了, 就不在队列里面了, 所以移除也没什么卵用
				mExecutor.getQueue().remove(r);
			}
		}

	}
}
