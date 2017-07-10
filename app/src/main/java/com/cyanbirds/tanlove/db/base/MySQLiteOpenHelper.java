package com.cyanbirds.tanlove.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cyanbirds.tanlove.greendao.ConversationDao;
import com.cyanbirds.tanlove.greendao.DaoMaster;
import com.cyanbirds.tanlove.greendao.DynamicDao;
import com.cyanbirds.tanlove.greendao.GoldDao;
import com.cyanbirds.tanlove.greendao.IMessageDao;
import com.cyanbirds.tanlove.greendao.NameListDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * 作者：wangyb
 * 时间：2017/7/4 22:44
 * 描述：
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {

	public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}
	@Override
	public void onUpgrade(Database db, int oldVersion, int newVersion) {
		MigrationHelper.migrate(db,
				ConversationDao.class,
				DynamicDao.class,
				GoldDao.class,
				IMessageDao.class,
				NameListDao.class);
	}
}
