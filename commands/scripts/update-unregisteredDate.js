/**
 * This script is used to read unregistered user data from a CSV file, 
 * then update MongoDB documents in the unregisteredUser collection with a new unregisteredDate. 
 */


const fs = require('fs');

// Hardcoded script parameters
// Reads the environment variable to get the CSV file path
const filePath = process.env.UNREGISTERED_USERS_DATA;
//MongoDB collection name
const collectionName = 'unregisteredUser';

print(`Start processing file ${filePath}`);

// Validate that the CSV file exists
if (!filePath || !fs.existsSync(filePath)) {
    print(`CSV file not found or path is not set: ${filePath}`);
    throw new Error(`CSV file not found or path is not set: ${filePath}`);
}

// Read the file and process each ID
const lines = fs.readFileSync(filePath, 'utf-8').split('\n').map(id => id.trim()).filter(id => id);
// Extract headers
const headers = lines[0].split(';').map(h => h.trim());
print(`start processing headers: ${headers}`);
// Find the index of the _id and unregistered_date fields in the header row
const idIndex = headers.indexOf('_id');
print(`index of consumerID: ${idIndex}`);
const unregisteredDateIndex = headers.indexOf('unregistered_date');
print(`index of unregistered_date: ${unregisteredDateIndex}`);

// Validate that required headers exist
if (idIndex === -1 || unregisteredDateIndex === -1) {
    print(`Missing required headers "_id" or "unregistered_date" in CSV file: ${filePath}`);
    throw new Error(`Missing required headers "_id" or "unregistered_date" in CSV file`);
}


// Extract IDs and corresponding unregistered dates from the file
const ids = lines.slice(1).map(line => line.split(';')[idIndex]);
const unregisteredDates = lines.slice(1).map(line => line.split(';')[unregisteredDateIndex]);




// Statistics counters
let updatedCount = 0;
let skippedCount = 0;
let failedCount = 0;
let totalCount = 0;
let unregisteredDateCounter = 0;


// Main loop through IDs
ids.forEach(id => {
    try {
        print(`start processing: ${id}`);
        totalCount++;
        const objectId = ObjectId(id);
        // Retrieve document by ID
        const doc = db[collectionName].findOne({ _id: objectId });

        if (doc) {
            // Set current Date
            let newUnregisteredDate = parseDateFromCsvAsUTC(unregisteredDates[unregisteredDateCounter]);
            print(`retrievedDate: ${unregisteredDates[unregisteredDateCounter]}`);
            print(`newUnregisteredDate: ${newUnregisteredDate}`);
            // Perform the update in MongoDB
            db[collectionName].updateOne(
                {_id: objectId},
                {$set: {unregisteredDate: newUnregisteredDate}}
            );
            updatedCount++;
            print(`Doc updated: ${id}`);


            
        } else { // Document not found in collection
            skippedCount++;
            print(`Skipped: document is missing: ${id}`);
        }
        
    } catch (e) {
        failedCount++;
        print(`Failed to process ID ${id}: ${e}`);
    }
    unregisteredDateCounter++;
});



/**
 * Parses a date string from the CSV file into a UTC JavaScript Date object.
 * Expected format: "YYYY-MM-DD HH:mm[:ss]"
 *
 * @param {string} dateStr - The date string from CSV
 * @returns {Date} - Parsed UTC date or throws if format is invalid
 */
function parseDateFromCsvAsUTC(dateStr) {

    // Reject empty or "NULL" values
    if (!dateStr || dateStr.trim().toUpperCase() === 'NULL') {
        throw new Error("Invalid date: input is empty or 'NULL'");
    }

    
    const parts = dateStr.trim().split(/\s+/);
    if (parts.length !== 2) {
        throw new Error(`Invalid date format: expected "YYYY-MM-DD HH:mm[:ss]", got "${dateStr}"`);
    }

    const [datePart, timePart] = parts;

    const [year, month, day] = datePart.split('-').map(Number);
    const [hours, minutes, seconds = 0] = timePart.split(':').map(Number);

    if ([year, month, day, hours, minutes, seconds].some(Number.isNaN)) {
        throw new Error(`Date contains invalid numeric parts: "${dateStr}"`);
    }

    // Use Date.UTC to ensure UTC date, and avoid local timezone shift
    const utcDate = new Date(Date.UTC(year, month - 1, day, hours, minutes, seconds));

    // validate date not 1970 or obviously wrong
    if (utcDate.getUTCFullYear() < 1970) {
        throw new Error(`Parsed date is invalid or too old: ${utcDate.toISOString()}`);
    }

    return utcDate;
}




// Print summary statistics
print("===== Update Summary =====");
print(`Total amount: ${totalCount}`);
print(`Updated: ${updatedCount}`);
print(`Skipped: ${skippedCount}`);
print(`Failed: ${failedCount}`);
print("==========================");

