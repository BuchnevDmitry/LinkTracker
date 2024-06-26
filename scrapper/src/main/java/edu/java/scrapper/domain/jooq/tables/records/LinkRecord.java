/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.tables.records;

import edu.java.scrapper.domain.jooq.tables.Link;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.17.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class LinkRecord extends UpdatableRecordImpl<LinkRecord>
    implements Record6<Long, String, OffsetDateTime, OffsetDateTime, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.LAST_CHECK_TIME</code>.
     */
    public void setLastCheckTime(@NotNull OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.LAST_CHECK_TIME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastCheckTime() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>LINK.CREATED_AT</code>.
     */
    public void setCreatedAt(@NotNull OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.CREATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>LINK.CREATED_BY</code>.
     */
    public void setCreatedBy(@NotNull String value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINK.CREATED_BY</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getCreatedBy() {
        return (String) get(4);
    }

    /**
     * Setter for <code>LINK.HASH_INT</code>.
     */
    public void setHashInt(@Nullable Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>LINK.HASH_INT</code>.
     */
    @Nullable
    public Long getHashInt() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row6<Long, String, OffsetDateTime, OffsetDateTime, String, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row6<Long, String, OffsetDateTime, OffsetDateTime, String, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Link.LINK.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Link.LINK.LAST_CHECK_TIME;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Link.LINK.CREATED_AT;
    }

    @Override
    @NotNull
    public Field<String> field5() {
        return Link.LINK.CREATED_BY;
    }

    @Override
    @NotNull
    public Field<Long> field6() {
        return Link.LINK.HASH_INT;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime component3() {
        return getLastCheckTime();
    }

    @Override
    @NotNull
    public OffsetDateTime component4() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public String component5() {
        return getCreatedBy();
    }

    @Override
    @Nullable
    public Long component6() {
        return getHashInt();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime value3() {
        return getLastCheckTime();
    }

    @Override
    @NotNull
    public OffsetDateTime value4() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public String value5() {
        return getCreatedBy();
    }

    @Override
    @Nullable
    public Long value6() {
        return getHashInt();
    }

    @Override
    @NotNull
    public LinkRecord value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@NotNull OffsetDateTime value) {
        setLastCheckTime(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@NotNull OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value5(@NotNull String value) {
        setCreatedBy(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value6(@Nullable Long value) {
        setHashInt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(
        @NotNull Long value1,
        @NotNull String value2,
        @NotNull OffsetDateTime value3,
        @NotNull OffsetDateTime value4,
        @NotNull String value5,
        @Nullable Long value6
    ) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({"id", "url", "lastCheckTime", "createdAt", "createdBy", "hashInt"})
    public LinkRecord(
        @NotNull Long id,
        @NotNull String url,
        @NotNull OffsetDateTime lastCheckTime,
        @NotNull OffsetDateTime createdAt,
        @NotNull String createdBy,
        @Nullable Long hashInt
    ) {
        super(Link.LINK);

        setId(id);
        setUrl(url);
        setLastCheckTime(lastCheckTime);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setHashInt(hashInt);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.scrapper.domain.jooq.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setId(value.getId());
            setUrl(value.getUrl());
            setLastCheckTime(value.getLastCheckTime());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setHashInt(value.getHashInt());
        }
    }
}
