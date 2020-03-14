package org.giiwa.alarmclient.task;

import org.giiwa.alarmclient.web.admin.alarmsetting;
import org.giiwa.bean.Data;
import org.giiwa.bean.GLog;
import org.giiwa.conf.Global;
import org.giiwa.dao.Helper.V;
import org.giiwa.dao.Helper.W;
import org.giiwa.engine.Velocity;
import org.giiwa.net.client.Http;
import org.giiwa.dao.Beans;
import org.giiwa.dao.X;
import org.giiwa.json.JSON;
import org.giiwa.task.Task;
import org.giiwa.web.Language;

public class UploadTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static UploadTask inst = new UploadTask();

	private boolean stop = false;

	@Override
	public String getName() {
		// the task name, MUST global unique in JVM
		return "alarm.upload.task";
	}

	@Override
	public void onFinish() {
		// re-run this task at a minute alter
		if (!stop) {
			this.schedule(X.AMINUTE, true);
		}
	}

	@Override
	public void onExecute() {

		if (Global.getInt("alarm.enabled", 0) == 0) {
			return;
		}

		String server = "https://alarm.giisoo.com/alarm";
		String uuid = Global.getString("app.uuid", null);
		String table = Global.getString("alarm.table", null);
		if (X.isEmpty(server) || X.isEmpty(uuid) || X.isEmpty(table)) {
			GLog.applog.error(alarmsetting.class, "setting", "配置错误: " + server + "//" + uuid + "//" + table,
					new Exception());
			stop = true;
			return;
		}

		String format = Global.getString("alarm.format", null);

		W q = W.create().and("created", System.currentTimeMillis() - X.ADAY, W.OP.gte).and("_upload", 1, W.OP.neq)
				.sort("created");

		Http h = Http.create();
		Language lang = Language.getLanguage();

		int s = 0;
		Beans<Data> l1 = Data.load(table, q, s, 100);
		while (l1 != null && !l1.isEmpty()) {
			l1.forEach(e -> {
				JSON j1 = JSON.create();
				if (X.isEmpty(format)) {
					j1.append("content", e.get("content"));
				} else {
					try {
						String s1 = Velocity.parse(format, e);
						j1.append("content", s1);
					} catch (Exception e1) {
						log.error(e1.getMessage(), e1);
					}
				}
				if (!j1.isEmpty()) {
					j1.append("uuid", uuid);
					j1.append("time", lang.format(e.getCreated(), "yyyy-MM-dd HH:mm:ss"));
					h.post(server, j1);
				}
				Data.update(table, W.create(X.ID, e.get("id")), V.create("_upload", 1));
			});
			s += l1.size();
			l1 = Data.load(table, q, s, 100);
		}
		h.close();
	}

	public static void start() {

		if (Global.getInt("alarm.enabled", 0) == 1) {
			inst.stop = false;
			inst.schedule(0, true);
		}
	}

	public static void stop() {
		inst.stop = true;
		inst.stop(true);
	}

}
