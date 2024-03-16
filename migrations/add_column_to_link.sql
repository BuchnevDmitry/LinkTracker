--liquibase formatted sql;

--changeset DmitryBuchnev:4
--comment add column hash
ALTER TABLE link ADD COLUMN hash_int bigint;
--rollback alter table link drop column hash;

