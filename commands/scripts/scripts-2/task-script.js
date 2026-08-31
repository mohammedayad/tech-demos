const query = {
    
    createdAt:{
            $gte: ISODate("2023-05-02T00:00:00Z"),
            $lte: ISODate("2025-12-31T23:59:59Z")
        },
        
        $and: [
        {
            $or:[
        
            { "debtor.country": "BEL" },
            { $and: [{ "debtor.country": null }, { "creditor.pqCountry": "BEL" }] }
            
            ] // end or
        }
        
        ] // end and
    
}
// Collection
const collection = db.getCollection("payment");

// Total documents
 //const totalPayments = collection.countDocuments({});

// Documents matching the query
const bpcPayments = collection.countDocuments(query);

//print("Total payments in collection: " + totalPayments);
print("BPC Payments matching deletion query: " + bpcPayments);