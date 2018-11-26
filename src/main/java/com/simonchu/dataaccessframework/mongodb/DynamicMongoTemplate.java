package com.simonchu.dataaccessframework.mongodb;

import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexOperationsProvider;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 动态mongotemplate
 *
 * @author zhaozan.chu
 * @date   2018/11/14 17:14
 *
 */
public class DynamicMongoTemplate implements MongoOperations, ApplicationContextAware, IndexOperationsProvider {
    private final AtomicReference<MongoTemplate> mongoTemplateAtomicReference;

    public DynamicMongoTemplate(MongoTemplate mongoTemplate){
        mongoTemplateAtomicReference = new AtomicReference<>(mongoTemplate);
    }

    public MongoTemplate setMongoTemplate(MongoTemplate mongoTemplate){
        return mongoTemplateAtomicReference.getAndSet(mongoTemplate);
    }

    public MongoTemplate getMongoTemplate(){
        return mongoTemplateAtomicReference.get();
    }

    public void removeMongoTemplate(){
        mongoTemplateAtomicReference.set(null);
    }

    @Override
    public String getCollectionName(Class<?> aClass) {
        return mongoTemplateAtomicReference.get().getCollectionName(aClass);
    }

    @Override
    public Document executeCommand(String s) {
        return mongoTemplateAtomicReference.get().executeCommand(s);
    }

    @Override
    public Document executeCommand(Document document) {
        return mongoTemplateAtomicReference.get().executeCommand(document);
    }

    @Override
    public Document executeCommand(Document document, ReadPreference readPreference) {
        return mongoTemplateAtomicReference.get().executeCommand(document,readPreference);
    }

    @Override
    public void executeQuery(Query query, String s, DocumentCallbackHandler documentCallbackHandler) {
        mongoTemplateAtomicReference.get().executeQuery(query,s,documentCallbackHandler);
    }

    @Override
    public <T> T execute(DbCallback<T> dbCallback) {
        return mongoTemplateAtomicReference.get().execute(dbCallback);
    }

    @Override
    public <T> T execute(Class<?> aClass, CollectionCallback<T> collectionCallback) {
        return mongoTemplateAtomicReference.get().execute(aClass,collectionCallback);
    }

    @Override
    public <T> T execute(String s, CollectionCallback<T> collectionCallback) {
        return mongoTemplateAtomicReference.get().execute(s,collectionCallback);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().stream(query,aClass);
    }

    @Override
    public <T> CloseableIterator<T> stream(Query query, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().stream(query,aClass,s);
    }

    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().createCollection(aClass);
    }

    @Override
    public <T> MongoCollection<Document> createCollection(Class<T> aClass, CollectionOptions collectionOptions) {
        return mongoTemplateAtomicReference.get().createCollection(aClass,collectionOptions);
    }

    @Override
    public MongoCollection<Document> createCollection(String s) {
        return mongoTemplateAtomicReference.get().createCollection(s);
    }

    @Override
    public MongoCollection<Document> createCollection(String s, CollectionOptions collectionOptions) {
        return mongoTemplateAtomicReference.get().createCollection(s,collectionOptions);
    }

    @Override
    public Set<String> getCollectionNames() {
        return mongoTemplateAtomicReference.get().getCollectionNames();
    }

    @Override
    public MongoCollection<Document> getCollection(String s) {
        return mongoTemplateAtomicReference.get().getCollection(s);
    }

    @Override
    public <T> boolean collectionExists(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().collectionExists(aClass);
    }

    @Override
    public boolean collectionExists(String s) {
        return mongoTemplateAtomicReference.get().collectionExists(s);
    }

    @Override
    public <T> void dropCollection(Class<T> aClass) {
        mongoTemplateAtomicReference.get().dropCollection(aClass);
    }

    @Override
    public void dropCollection(String s) {
        mongoTemplateAtomicReference.get().dropCollection(s);
    }

    @Override
    public IndexOperations indexOps(String s) {
        return mongoTemplateAtomicReference.get().indexOps(s);
    }

    @Override
    public IndexOperations indexOps(Class<?> aClass) {
        return mongoTemplateAtomicReference.get().indexOps(aClass);
    }

    @Override
    public ScriptOperations scriptOps() {
        return mongoTemplateAtomicReference.get().scriptOps();
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, String s) {
        return mongoTemplateAtomicReference.get().bulkOps(bulkMode,s);
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().bulkOps(bulkMode,aClass);
    }

    @Override
    public BulkOperations bulkOps(BulkOperations.BulkMode bulkMode, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().bulkOps(bulkMode,aClass,s);
    }

    @Override
    public <T> List<T> findAll(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findAll(aClass);
    }

    @Override
    public <T> List<T> findAll(Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findAll(aClass,s);
    }

    @Override
    public <T> GroupByResults<T> group(String s, GroupBy groupBy, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().group(s,groupBy,aClass);
    }

    @Override
    public <T> GroupByResults<T> group(Criteria criteria, String s, GroupBy groupBy, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().group(criteria,s,groupBy,aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, String s, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregate(typedAggregation,s,aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(TypedAggregation<?> typedAggregation, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregate(typedAggregation,aClass);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, Class<?> aClass, Class<O> aClass1) {
        return mongoTemplateAtomicReference.get().aggregate(aggregation,aClass,aClass1);
    }

    @Override
    public <O> AggregationResults<O> aggregate(Aggregation aggregation, String s, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregate(aggregation,s,aClass);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> typedAggregation, String s, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregateStream(typedAggregation,s,aClass);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(TypedAggregation<?> typedAggregation, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregateStream(typedAggregation,aClass);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, Class<?> aClass, Class<O> aClass1) {
        return mongoTemplateAtomicReference.get().aggregateStream(aggregation,aClass,aClass1);
    }

    @Override
    public <O> CloseableIterator<O> aggregateStream(Aggregation aggregation, String s, Class<O> aClass) {
        return mongoTemplateAtomicReference.get().aggregateStream(aggregation,s,aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().mapReduce(s,s1,s2,aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().mapReduce(s,s1,s2,mapReduceOptions,aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().mapReduce(query,s,s1,s2,aClass);
    }

    @Override
    public <T> MapReduceResults<T> mapReduce(Query query, String s, String s1, String s2, MapReduceOptions mapReduceOptions, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().mapReduce(query,s,s1,s2,mapReduceOptions,aClass);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().geoNear(nearQuery,aClass);
    }

    @Override
    public <T> GeoResults<T> geoNear(NearQuery nearQuery, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().geoNear(nearQuery,aClass,s);
    }

    @Override
    public <T> T findOne(Query query, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findOne(query,aClass);
    }

    @Override
    public <T> T findOne(Query query, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findOne(query,aClass,s);
    }

    @Override
    public boolean exists(Query query, String s) {
        return mongoTemplateAtomicReference.get().exists(query,s);
    }

    @Override
    public boolean exists(Query query, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().exists(query,aClass);
    }

    @Override
    public boolean exists(Query query, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().exists(query,aClass,s);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().find(query,aClass);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().find(query,aClass,s);
    }

    @Override
    public <T> T findById(Object o, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findById(o,aClass);
    }

    @Override
    public <T> T findById(Object o, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findById(o,aClass,s);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findAndModify(query,update,aClass);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findAndModify(query,update,aClass,s);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findAndModify(query,update,findAndModifyOptions,aClass);
    }

    @Override
    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions findAndModifyOptions, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findAndModify(query,update,findAndModifyOptions,aClass,s);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findAndRemove(query,aClass);
    }

    @Override
    public <T> T findAndRemove(Query query, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findAndRemove(query,aClass,s);
    }

    @Override
    public long count(Query query, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().count(query,aClass);
    }

    @Override
    public long count(Query query, String s) {
        return mongoTemplateAtomicReference.get().count(query,s);
    }

    @Override
    public long count(Query query, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().count(query,aClass,s);
    }

    @Override
    public void insert(Object o) {
        mongoTemplateAtomicReference.get().insert(o);
    }

    @Override
    public void insert(Object o, String s) {
        mongoTemplateAtomicReference.get().insert(o,s);
    }

    @Override
    public void insert(Collection<?> collection, Class<?> aClass) {
        mongoTemplateAtomicReference.get().insert(collection,aClass);
    }

    @Override
    public void insert(Collection<?> collection, String s) {
        mongoTemplateAtomicReference.get().insert(collection,s);
    }

    @Override
    public void insertAll(Collection<?> collection) {
        mongoTemplateAtomicReference.get().insertAll(collection);
    }

    @Override
    public void save(Object o) {
        mongoTemplateAtomicReference.get().save(o);
    }

    @Override
    public void save(Object o, String s) {
        mongoTemplateAtomicReference.get().save(o,s);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().upsert(query,update,aClass);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, String s) {
        return mongoTemplateAtomicReference.get().upsert(query,update,s);
    }

    @Override
    public UpdateResult upsert(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().upsert(query,update,aClass,s);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().updateFirst(query,update,aClass);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, String s) {
        return mongoTemplateAtomicReference.get().updateFirst(query,update,s);
    }

    @Override
    public UpdateResult updateFirst(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().updateFirst(query,update,aClass,s);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().updateMulti(query,update,aClass);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, String s) {
        return mongoTemplateAtomicReference.get().updateMulti(query,update,s);
    }

    @Override
    public UpdateResult updateMulti(Query query, Update update, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().updateMulti(query,update,aClass,s);
    }

    @Override
    public DeleteResult remove(Object o) {
        return mongoTemplateAtomicReference.get().remove(o);
    }

    @Override
    public DeleteResult remove(Object o, String s) {
        return mongoTemplateAtomicReference.get().remove(o,s);
    }

    @Override
    public DeleteResult remove(Query query, Class<?> aClass) {
        return mongoTemplateAtomicReference.get().remove(query,aClass);
    }

    @Override
    public DeleteResult remove(Query query, Class<?> aClass, String s) {
        return mongoTemplateAtomicReference.get().remove(query,aClass,s);
    }

    @Override
    public DeleteResult remove(Query query, String s) {
        return mongoTemplateAtomicReference.get().remove(query,s);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, String s) {
        return mongoTemplateAtomicReference.get().findAllAndRemove(query,s);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass) {
        return mongoTemplateAtomicReference.get().findAllAndRemove(query,aClass);
    }

    @Override
    public <T> List<T> findAllAndRemove(Query query, Class<T> aClass, String s) {
        return mongoTemplateAtomicReference.get().findAllAndRemove(query,aClass,s);
    }

    @Override
    public MongoConverter getConverter() {
        return mongoTemplateAtomicReference.get().getConverter();
    }

    @Override
    public <T> ExecutableAggregation<T> aggregateAndReturn(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().aggregateAndReturn(aClass);
    }

    @Override
    public <T> ExecutableFind<T> query(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().query(aClass);
    }

    @Override
    public <T> ExecutableInsert<T> insert(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().insert(aClass);
    }

    @Override
    public <T> ExecutableRemove<T> remove(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().remove(aClass);
    }

    @Override
    public <T> ExecutableUpdate<T> update(Class<T> aClass) {
        return mongoTemplateAtomicReference.get().update(aClass);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        mongoTemplateAtomicReference.get().setApplicationContext(applicationContext);
    }
}
