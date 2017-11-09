package com.totalplay.datamanager;

import android.content.Context;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by totalplay on 3/7/17.
 * EMP
 */

public abstract class BaseDataManager {

    public Realm mRealm;

    public BaseDataManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public static void init(Context context) {
        Realm.init(context);
        byte[] key = new byte[64];
        new SecureRandom(key);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded().
                        encryptionKey(key).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void validateConnection() {
        if (mRealm == null || mRealm.isClosed())
            mRealm = Realm.getDefaultInstance();
    }

    public void close() {
        if (mRealm != null && !mRealm.isClosed()) mRealm.close();
    }

    public void clearAll() {
        mRealm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    /////////////////////////////////////////// BASE QUERY

    public <T extends RealmModel> void findObject(Class<T> tClass, String id, ValidatorResult<T> validatorResult) {
        T object = queryWhere(tClass).filter("id", id).findFirst();
        if (object != null) {
            validatorResult.result(tClass.cast(object));
        }
    }

    public <T extends RealmModel> void findByCodeObject(Class<T> tClass, String id, ValidatorResult<T> validatorResult) {
        T object = queryWhere(tClass).filter("code", id).findFirst();
        if (object != null) {
            validatorResult.result(tClass.cast(object));
        }
    }

    public interface ValidatorResult<T extends RealmModel> {
        void result(T object);
    }

    public <T extends RealmModel> Query queryWhere(Class<T> tClass) {
        return new Query().create(tClass);
    }

    public <T extends RealmModel> T findObjectOfAttention(Class<T> tClass, String attentionId) {
        return new Query().create(tClass).filter("attention", attentionId).findFirst();
    }

    private <T extends RealmModel> ArrayList<T> findObjectsOfAttention(Class<T> tClass, String attentionId) {
        RealmResults<T> objects = mRealm.where(tClass)
                .equalTo("attention", attentionId).findAll();
        return new ArrayList<>(mRealm.copyFromRealm(objects));
    }

    ///////////////////////////////////////// MODEL QUERY

//    public interface TX {
//        void execute(BaseDataManager.Transactions tx);
//    }

    @SuppressWarnings("unchecked")
    public class Query {

        private RealmQuery realmQuery;
        private String attrSorted = "";

        public <T extends RealmModel> Query create(Class<T> tClass) {
            validateConnection();
            realmQuery = mRealm.where(tClass);
            return this;
        }

        public Query filter(String filter, String value) {
            realmQuery.equalTo(filter, value);
            return this;
        }

        public Query filterWhereNot(String filter, String value) {
            realmQuery.notEqualTo(filter, value);
            return this;
        }

        public Query filter(String filter, boolean value) {
            realmQuery.equalTo(filter, value);
            return this;
        }

        public Query filter(String filter, Date value) {
            realmQuery.equalTo(filter, value);
            return this;
        }

        public Query between(String filter, Date valueOne, Date valueTwo) {
            realmQuery.between(filter, valueOne, valueTwo);
            return this;
        }

        public <T extends RealmModel> T findFirstId(String value) {
            T result = (T) realmQuery.equalTo("id", value).findFirst();
            return result != null ? mRealm.copyFromRealm(result) : null;
        }

        public <T extends RealmModel> T findFirst() {
            T result = (T) realmQuery.findFirst();
            return result != null ? mRealm.copyFromRealm(result) : null;
        }

        public void remove() {
            RealmResults results = realmQuery.findAll();
            if (results != null) results.deleteAllFromRealm();
        }

        public <T extends RealmModel> ArrayList<T> list() {
            return
                    attrSorted.equals("") ? new ArrayList<>(mRealm.copyFromRealm(realmQuery.findAll()))
                            : new ArrayList<>(mRealm.copyFromRealm(realmQuery.findAllSorted(attrSorted)));
        }

        public Query order(String attrSorted) {
            this.attrSorted = attrSorted;
            return this;
        }
    }


//    @SuppressWarnings("WeakerAccess")
//    public static class Transactions {
//
//        public BaseDataManager dataManager;
//        public Realm realm;
//
//        Transactions(Realm realm, BaseDataManager dataManager) {
//            this.realm = realm;
//            this.dataManager = dataManager;
//        }
//
//
//    }
}
