## 1. Written Approach (Before Coding)

### High-Level Approach

I have built a **Spring Boot** application using **Hibernate JPA** as the ORM framework with **PostgreSQL** as the
database. The system provides REST APIs to upload CSV files, validate data, and ingest records into respective database
tables.

### Validation Strategy

**Three-Tier Validation Approach:**

1. **Application-Level Validation (Jakarta Validation)**
    - `@NotBlank`, `@NotNull`, `@NotEmpty` for required fields
    - `@Size`, `@Pattern`, `@Email` for format validation
    - `@PastOrPresent` for date fields
    - Validation happens before any database operation

2. **Service-Level Validation**
    - Business rule validation (e.g., checking if referenced records exist)
    - Foreign key validation (user exists, store exists)
    - Custom validation for unique constraints

3. **Database-Level Constraints**
    - Unique constraints (`@UniqueConstraint`)
    - Check constraints (`@Check`)
    - Foreign key constraints with `ON DELETE SET NULL/CASCADE`
    - Not-null constraints

### Performance Strategy for 5-Lakh (500K) Row File

1. **Current Approach (Sync Mode):**
    - Each row processed in its own transaction
    - `saveAndFlush()` for immediate persistence
    - `entityManager.clear()` after each row to prevent memory issues
    - Error logging to file without stopping the process

   **Results Achieved:**
    - **500,000 rows processed** in ~55 minutes
    - **494,260 successful** (98.85% success rate)
    - **5,740 failed rows** (1.15% failure rate)

2. **BatchSaving Approach With 100 Consecutive Rows(Sync Mode)**
    - simultaneously 100 rows processed using saveAll method
    - with immediate persistence

   **Results Achieved:**
    - **500,000 rows processed** in ~40 minutes
    - **186,600 successful** (37.32% success rate)
    - **313,400 failed rows** (62.68% failure rate)

### Row Failure Handling Strategy

**Decision: SKIP failed rows, DO NOT reject entire file**

**Reasons:**

1. **Data Independence:** One bad row shouldn't block 499,999 good rows
2. **Real-world Scenario:** CSV files often have corrupt/malformed rows
3. **Auditability:** Failed rows logged for later investigation and re-upload
4. **Business Continuity:** Partial data is better than no data
5. **User Experience:** User gets most data imported, only problematic rows reported

**What happens on failure:**

- Row is logged to `csv_import_error.log` with complete details
- Transaction for that row is rolled back
- Processing continues with next row
- Final summary shows success/failure counts

### Tradeoffs Considered

1. **In Single Row Processing**
    - Success Rate is High, Which is Around 98% But time taken is very High, Which is 55 Minute.
2. **In Batch Processing**
    - Success Rate is Very Low. Since 100 Rows are persisted into the DB at the same time, So If one row failed then all
      the successful rows will roll back.
    - Time Taken is 15 minute less than previous But success rate is only 37%.

## 2. Technology Stack

```
Framework: Spring Boot 3.x
ORM: Hibernate JPA
Database: PostgreSQL
CSV Parsing: OpenCSV
Build Tool: Maven
Java Version: 17/21
Validation: Jakarta Bean Validation
```

---

## 3. Performance Evidence - 500K File Import

### Test Environment

```
- File: stores_master_500k.csv
- Total Rows: 500,000
- Database: PostgreSQL (local)
- Processing Mode: Synchronous, Single-row transactions
```

### Performance Metrics

| Metric                 | Value                         |
|------------------------|-------------------------------|
| **Total Rows**         | 500,000                       |
| **Successful Inserts** | 494,260                       |
| **Failed Rows**        | 5,740                         |
| **Success Rate**       | 98.85%                        |
| **Total Duration**     | 3,353,263 ms (~55.89 minutes) |
| **Average Speed**      | ~149 rows/second              |
| **Time per Row**       | ~6.7 ms                       |

| Metric                 | Value                         |
|------------------------|-------------------------------|
| **Total Rows**         | 500,000                       |
| **Successful Inserts** | 186,600                       |
| **Failed Rows**        | 313,400                       |
| **Success Rate**       | 37.32%                        |
| **Total Duration**     | 2,424,663 ms (~40.41 minutes) |
| **Average Speed**      | ~206 rows/second              |
| **Time per Row**       | ~0.5 s/100Row                 |

### Failure Analysis

**Failed rows (5,740) - Common causes:**

1. Duplicate unique constraint violations
2. Referenced user/store not found
3. Invalid date format
4. Data type mismatches

All failed rows are logged in:

- `csv_import_error_500k_inBulk_100.log` - Complete error details for batch processing with 100 consecutive rows
- `csv_import_error_500k.log` - Complete error details for single row processing.
- `csv_import_errors.log` - Complete error details for single row processing store_master,user_master, and
  store_user_map file.
- `csv_import_summary.log` - Summary statistics

### Comparative Performance

| File            | Rows        | Duration         | Success Rate |
|-----------------|-------------|------------------|--------------|
| Store Master    | 100         | 1,349 ms         | 95%          |
| User Master     | 30          | 180 ms           | 83.3%        |
| User Mapping    | 150         | 1,196 ms         | 72%          |
| **500K Master** | **500,000** | **3,353,263 ms** | **98.85%**   |

### Why 55 Minutes for 500K Rows?

1. **Single-row transactions:** Each of 500,000 rows has its own transaction
2. **Save and flush after each row:** Ensures data persistence immediately
3. **EntityManager clear after each row:** Prevents memory issues but adds overhead
4. **Foreign key lookups:** Each row queries reference tables (brands, types, cities, etc.)
5. **Validation overhead:** Application and database validation for each row

### Why Only 37% Success Rate for 500K Rows In Batch Processing?

1. **100-row transactions:** Each 100 consecutive rows from 500,000 has its own transaction
2. **Save and flush after each 100 row transaction:** Ensures data persistence immediately
3. **EntityManager clear after each row:** Prevents memory issues but adds overhead
4. **Foreign key lookups:** Each row queries reference tables (brands, types, cities, etc.)
5. **Validation overhead:** Application and database validation for each row
6. **Majorly Due to Roll back Functionality:** In Batch Processing if a single row fail, then all the successful row
   also go through rollback stage.

## 4. API Endpoints

### File Upload Endpoints

```bash
# Upload Store Master CSV
POST /upload/store-master

# Upload User Master CSV  
POST /upload/user-master

# Upload Store-User Mapping CSV
POST /upload/store-user-mapping

# Upload Large 500K File
POST /upload/stores-master-500k
```

### Import Processing Endpoints

```bash
# Start Store Master Import
POST /process/store-master

# Start User Master Import
POST /process/user-master

# Start Mapping Import
POST /process/store-user-mapping

# Start 500K store master Import
POST /process/store-master-500K
```

## 5. Error Handling & Logging

### Error Log Files Generated

| File                        | Purpose                                   |
|-----------------------------|-------------------------------------------|
| `csv_import_error.log`      | Individual row failures with stack traces |
| `csv_import_error_500k.log` | 500K file specific errors                 |
| `csv_import_summary.log`    | Import summaries with statistics          |

### Sample Error Log Entry

```
========================================
FILE: stores_master_500k.csv
ROW: 15234
DATA: STR-9999,EXT-123,Store Name,Title,Brand,Type,...
ERROR: Duplicate key value violates unique constraint "uk_store_id"
TIME: 2026-04-03T03:15:22.456
========================================
```

### Sample Summary Log

```
========================================
IMPORT SUMMARY
FILE: stores_master_500k.csv
TOTAL ROWS: 500000
SUCCESSFUL: 494260
FAILED: 5740
DURATION: 3353263 ms
COMPLETION TIME: 2026-04-03T03:55:17.646
========================================
```

## 6. Conclusion

The assignment has been successfully completed with:

✅ **Working Spring Boot application** with REST APIs  
✅ **Three-tier validation** (app, service, database)  
✅ **500K row processing** with 98.85% success rate  
✅ **Error logging** to separate files  
✅ **Skip-on-failure strategy** implemented  
✅ **Performance metrics** captured and documented

**Total time for 500K rows:** 55 minutes (sync mode)  
**Total time for 500K rows in Batch Processing(100rows):** 40 minute (sync mode)

All code is production-ready and testable via Postman/curl commands.

**Note:** Detailed error logs available in `csv_import_error.log` and `csv_import_summary.log` files in the project root
directory.