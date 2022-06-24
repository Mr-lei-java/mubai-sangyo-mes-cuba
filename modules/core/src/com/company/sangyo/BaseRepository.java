package com.company.sangyo;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.ValueLoadContext;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@NoRepositoryBean
public interface BaseRepository<T extends Entity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

//public interface BaseRepository<T extends Entity<ID>, ID extends Serializable> {
    /**
     * 通过DataManager加载单个实体对象，核心通过DataManager的load方法
     *
     * @param id   实体对象ID
     * @param view 查询视图
     * @return 加载的实体对象
     */
    T load(ID id, String view);

    /**
     * 通过DataManager加载单个实体对象，核心通过DataManager的load方法
     *
     * @param where      查询JPQL中的where子句
     * @param parameters 参数对映射（Map）
     * @param view       查询视图
     * @return 加载的实体对象
     */
    T load(String where, Map<String, Object> parameters, String view);

    /**
     * 通过DataManager加载单个实体对象，核心通过DataManager的load方法
     *
     * @param loadContext 加载上下文
     * @return 加载的实体对象
     */
    T load(LoadContext<T> loadContext);

    /**
     * 通过DataManager加载实体对象列表，核心通过DataManager的loadList方法
     *
     * @param view 查询视图
     * @return 加载的实体对象列表（List）
     */
    List<T> loadList(String view);

    /**
     * 通过DataManager加载实体对象列表，核心通过DataManager的loadList方法
     *
     * @param where      查询JPQL中的where子句
     * @param parameters 参数对映射（Map）
     * @param view       查询视图
     * @return 加载的实体对象列表（List）
     */
    List<T> loadList(String where, Map<String, Object> parameters, String view);

    /**
     * 通过DataManager加载实体对象列表，核心通过DataManager的loadList方法
     *
     * @param loadContext 加载上下文
     * @return 加载的实体对象列表（List）
     */
    List<T> loadList(LoadContext<T> loadContext);

    /**
     * 通过DataManager加载多个键值对实体对象，核心通过Data的loadValues方法
     *
     * @param properties 属性名列表
     * @return 加载的键值对实体对象（List）
     */
    List<KeyValueEntity> loadValues(List<String> properties);

    /**
     * 通过DataManager加载多个键值对实体对象，核心通过Data的loadValues方法
     *
     * @param where      查询JPQL中的where子句
     * @param parameters 参数对映射（Map）
     * @param properties 属性名列表
     * @return 加载的键值对实体对象（List）
     */
    List<KeyValueEntity> loadValues(String where, Map<String, Object> parameters, List<String> properties);

    /**
     * 通过DataManager加载多个键值对实体对象，核心通过Data的loadValues方法
     *
     * @param valueLoadContext 值加载上下文
     * @return 加载的键值对实体对象（List）
     */
    List<KeyValueEntity> loadValues(ValueLoadContext valueLoadContext);

    /**
     * 通过DataManager获取所有实体记录数量，核心通过DataManager的getCount方法
     *
     * @return 记录数量
     */
    long getCount();

    /**
     * 通过Data获取所有实体记录数量，核心通过DataManager的getCount方法
     *
     * @param where      查询JPQL中的where子句
     * @param parameters 参数对映射（Map）
     * @return 记录数量
     */
    long getCount(String where, Map<String, Object> parameters);

    /**
     * 通过Data获取所有实体记录数量，核心通过DataManager的getCount方法
     *
     * @param loadContext 加载上下文
     * @return 记录数量
     */
    long getCount(LoadContext<T> loadContext);

    /**
     * 通过DataManager提交实体对象，核心通过DataManager的commit方法
     *
     * @param entity 实体对象
     * @return 提交的实体对象
     */
    T commit(T entity);

    /**
     * 通过DataManager提交实体对象，核心通过DataManager的commit方法
     *
     * @param entity 实体对象
     * @param view   查询视图，影响到返回的实体对象
     * @return 提交的实体对象
     */
    T commit(T entity, String view);

    /**
     * 通过DataManager提交实体对象，核心通过DataManager的commit方法
     * 注意CommitContext不限制数据类型，也就是说上下文可以混合各种实体
     *
     * @param commitContext 提交上下文
     * @return 提交的实体对象集合
     */
    Set<Entity> commit(CommitContext commitContext);

    /**
     * 通过DataManager重新加载实体对象，核心通过DataManager的reload方法
     * 此情况下将会更加实体对象是否有动态属性来决定是否加载动态属性
     *
     * @param entity 待重新加载实体对象
     * @param view   查询视图
     * @return 加载的实体对象
     */
    T reload(T entity, String view);

    /**
     * 通过DataManager重新加载实体对象，核心通过DataManager的reload方法
     *
     * @param entity                待重新加载实体对象
     * @param view                  查询视图
     * @param loadDynamicAttributes 是否需要加载动态属性
     * @return 加载的实体对象
     */
    T reload(T entity, String view, boolean loadDynamicAttributes);

    /**
     * 通过DataManager删除实体对象，核心通过DataManager的remove方法
     *
     * @param entity 待删除实体对象
     */
    void remove(T entity);
}
