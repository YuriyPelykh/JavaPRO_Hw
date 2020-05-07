package com.gmail.yuriypelykh;

import javax.persistence.*;

public class Services {

    public static void flushEntity(EntityManager em, Object obj) {
        em.getTransaction().begin();
        try {
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }




}
