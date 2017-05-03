package hylk.com.xiaochekaoqin.bean;

/**
 * Created by _wzz on 2016/8/10 09:44.
 */
public class LoginBean {

	public int Success;
	public String Message;
	public RerurnValue RerurnValue;

	public class RerurnValue {

		public int KgId;
		public String KgName;
		public String Name;
		public int UserId;

		public int WorkerExtensionId ;
		public String Permissions ;
		public int OrgCategory ;
	}
}
