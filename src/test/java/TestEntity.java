public class TestEntity {

    private long id;
    private String entityName;

    public TestEntity(long id, String entityName) {
        this.id = id;
        this.entityName = entityName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
