package com.cyanbirds.tanlove.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAT_LIMIT".
*/
public class ChatLimitDao extends AbstractDao<ChatLimit, Long> {

    public static final String TABLENAME = "CHAT_LIMIT";

    /**
     * Properties of entity ChatLimit.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property Count = new Property(2, int.class, "count", false, "COUNT");
    }


    public ChatLimitDao(DaoConfig config) {
        super(config);
    }
    
    public ChatLimitDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAT_LIMIT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT NOT NULL UNIQUE ," + // 1: userId
                "\"COUNT\" INTEGER NOT NULL );"); // 2: count
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAT_LIMIT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChatLimit entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
        stmt.bindLong(3, entity.getCount());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChatLimit entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserId());
        stmt.bindLong(3, entity.getCount());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChatLimit readEntity(Cursor cursor, int offset) {
        ChatLimit entity = new ChatLimit( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // userId
            cursor.getInt(offset + 2) // count
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChatLimit entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setCount(cursor.getInt(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChatLimit entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChatLimit entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChatLimit entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
