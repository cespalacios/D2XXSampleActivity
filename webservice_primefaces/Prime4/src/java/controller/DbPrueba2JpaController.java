/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import loginBeanPack.DbPrueba2;

/**
 *
 * @author mnrohoden
 */
public class DbPrueba2JpaController implements Serializable {

    public DbPrueba2JpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DbPrueba2 dbPrueba2) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            em.persist(dbPrueba2);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDbPrueba2(dbPrueba2.getId()) != null) {
                throw new PreexistingEntityException("DbPrueba2 " + dbPrueba2 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DbPrueba2 dbPrueba2) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            dbPrueba2 = em.merge(dbPrueba2);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dbPrueba2.getId();
                if (findDbPrueba2(id) == null) {
                    throw new NonexistentEntityException("The dbPrueba2 with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DbPrueba2 dbPrueba2;
            try {
                dbPrueba2 = em.getReference(DbPrueba2.class, id);
                dbPrueba2.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dbPrueba2 with id " + id + " no longer exists.", enfe);
            }
            em.remove(dbPrueba2);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DbPrueba2> findDbPrueba2Entities() {
        return findDbPrueba2Entities(true, -1, -1);
    }

    public List<DbPrueba2> findDbPrueba2Entities(int maxResults, int firstResult) {
        return findDbPrueba2Entities(false, maxResults, firstResult);
    }

    private List<DbPrueba2> findDbPrueba2Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DbPrueba2.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DbPrueba2 findDbPrueba2(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DbPrueba2.class, id);
        } finally {
            em.close();
        }
    }

    public int getDbPrueba2Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DbPrueba2> rt = cq.from(DbPrueba2.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
