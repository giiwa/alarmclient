package org.giiwa.alarmclient.web;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.giiwa.app.web.admin.setting;
import org.giiwa.bean.License;
import org.giiwa.alarmclient.task.UploadTask;
import org.giiwa.alarmclient.web.admin.alarmsetting;
import org.giiwa.web.IListener;
import org.giiwa.web.Module;

public class AlarmclientListener implements IListener {

	static Log log = LogFactory.getLog(AlarmclientListener.class);

	public void onInit(Configuration conf, Module m) {
		log.warn("webalarmclient is initing...");

		m.setLicense(License.LICENSE.free,
				"QXw5/PkGMfWgxYKiU4e1Z5c69+7TvwMyk6mbKbxnsCMxl7G/4pPAWrNPjNEbHu/l/FD3nIxnGviM+hU1J1pRGVNRgfYvKURDPhWziS89XRWnuTFss4zr+1irjGGtW2F4yNJ1ykQ+0UMqAMrUdmsN1UXC110Hvk4dAmxKTzlY10I=");

		setting.register("alarm", alarmsetting.class);

	}

	@Override
	public void onStart(Configuration conf, Module m) {
		// TODO Auto-generated method stub
		log.warn("webalarmclient is starting ...");

		UploadTask.start();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		log.warn("webalarmclient is stopping ...");

	}

	@Override
	public void uninstall(Configuration conf, Module m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void upgrade(Configuration conf, Module m) {
		// TODO Auto-generated method stub

	}

}
