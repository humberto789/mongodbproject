package com.imd.mongodbproject.service;

import com.imd.mongodbproject.model.User;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    private final String COLLECTION_NAME = "users";


    public User createUser(User user) {
        long nextId = sequenceGenerator.getNextUserId();
        user.setId(nextId);

        return mongoTemplate.save(user, COLLECTION_NAME);
    }

    public List<User> findAllUsers(Document queryDoc, Document fieldsDoc, Integer page, Integer size) {
        if (queryDoc == null) {
            queryDoc = new Document();
        }
        if (fieldsDoc == null) {
            fieldsDoc = new Document();
        }

        BasicQuery basicQuery = new BasicQuery(queryDoc, fieldsDoc);

        if (page != null && size != null && page >= 0 && size > 0) {
            basicQuery.skip((long) page * size);
            basicQuery.limit(size);
        }

        return mongoTemplate.find(basicQuery, User.class, "users");
    }

    public User findById(long id) {
        Query query = new Query(where("id").is(id));
        return mongoTemplate.findOne(query, User.class, COLLECTION_NAME);
    }

    public User updateUser(long id, User userBody) {
        User existing = findById(id);
        if (existing == null) {
            return null;
        }

        existing.setName(userBody.getName());
        existing.setAge(userBody.getAge());

        return mongoTemplate.save(existing, COLLECTION_NAME);
    }
}
