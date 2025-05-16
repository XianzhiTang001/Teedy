package com.sismics.docs.core.dao;

import com.google.common.base.Strings;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sismics.docs.core.constant.Constants;
import com.sismics.docs.core.model.jpa.UserRequest;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.*;

public class UserRequestDao {
    public String create(UserRequest userRequest) throws Exception {
        userRequest.setId(UUID.randomUUID().toString());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query q = em.createQuery("select r from UserRequest r where r.username = :username");
        q.setParameter("username", userRequest.getUsername());
        List<?> l = q.getResultList();
        if (l.size() > 0) {
            throw new Exception("AlreadyExistingUsername");
        }

        userRequest.setCreateDate(new Date());
        userRequest.setPassword(userRequest.getPassword());
        userRequest.setStatus("PENDING");
        em.persist(userRequest);
        return userRequest.getId();
    }

    public void update(UserRequest userRequest) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();

        // Get the req
        Query q = em.createQuery("select r from UserRequest r where r.id = :id");
        q.setParameter("id", userRequest.getId());
        UserRequest reqDb = (UserRequest) q.getSingleResult();

        reqDb.setStatus(userRequest.getStatus());
    }

    public UserRequest getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        try {
            return em.find(UserRequest.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserRequest> getPendingRequests() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();

        Query q = em.createQuery("select r from UserRequest r where r.status = :status");
        q.setParameter("status", "PENDING");
        return q.getResultList();
    }
}