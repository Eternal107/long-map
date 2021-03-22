import jun.task.LongMapOpenAddressingImpl;
import jun.task.LongMapSeparateChainingImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LongMapSeparateChainingImpUnitTest {
    @Test
    public void getExistingValue() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");
        TestEntity entity2 = new TestEntity(2,"Car entity");

        map.put(entity1.getId(),entity1);
        map.put(entity2.getId(),entity2);

        TestEntity test = map.get(1L);

        assertEquals("Person entity", test.getEntityName());
    }

    @Test
    public void getNonExistingValue() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");
        TestEntity entity2 = new TestEntity(2,"Car entity");

        map.put(entity1.getId(),entity1);
        map.put(entity2.getId(),entity2);

        TestEntity test = map.get(3L);

        assertNull(test);
    }

    @Test
    public void getExistingValueAfterSameKeyInsertedTwice() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");
        TestEntity entity2 = new TestEntity(2,"Car entity");
        TestEntity entity3 = new TestEntity(1,"Animal entity");

        map.put(entity1.getId(),entity1);
        map.put(entity2.getId(),entity2);
        map.put(entity3.getId(),entity3);

        TestEntity test = map.get(1L);

        assertEquals(entity3.getEntityName(), test.getEntityName());
    }

    @Test
    public void insertSameObjectWithDifferentKey() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");

        map.put(entity1.getId(),entity1);
        map.put(2L,entity1);

        assertSame(map.get(entity1.getId()), map.get(2L));
    }

    @Test
    public void checkIfKeyExists() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");

        map.put(entity1.getId(),entity1);

        assertTrue(map.containsKey(1L));
    }

    @Test
    public void checkIfValueExists() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");

        map.put(entity1.getId(),entity1);

        assertTrue(map.containsValue(entity1));
    }

    @Test
    public void removeExistingKey() {
        LongMapSeparateChainingImpl<TestEntity> map = new LongMapSeparateChainingImpl<>();

        TestEntity entity1 = new TestEntity(1,"Person entity");
        TestEntity entity2 = new TestEntity(2,"Car entity");

        map.put(entity1.getId(),entity1);
        map.put(entity2.getId(),entity2);

        map.remove(entity1.getId());

        assertNull(map.get(entity1.getId()));
    }
}
