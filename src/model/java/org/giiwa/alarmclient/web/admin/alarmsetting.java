package org.giiwa.alarmclient.web.admin;

import org.giiwa.conf.Global;
import org.giiwa.dao.X;

public class alarmsetting extends org.giiwa.app.web.admin.setting {

	@Override
	public void get() {

		this.settingPage("/admin/alarm.setting.html");

	}

	@Override
	public void set() {

		Global.setConfig("alarm.enabled", X.isSame(this.getString("alarm.enabled"), "on") ? 1 : 0);
		Global.setConfig("app.uuid", this.getString("app.uuid"));
		Global.setConfig("alarm.table", this.getString("alarm.table"));
		Global.setConfig("alarm.format", this.getHtml("alarm.format"));

		this.set(X.MESSAGE, lang.get("save.success")).send(200);

	}

}
