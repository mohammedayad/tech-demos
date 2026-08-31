db.getCollection("payment").find({
   // for created date range 1815856 payments
        createdAt:{
            $gte: ISODate("2023-05-02T00:00:00Z"),
            $lte: ISODate("2025-12-31T23:59:59Z")
        },
        $and: [
        {
            $or:[
            
           { "debtor.country": "BEL" }, // 541980 payments alone
            
            { $and: [{ "debtor.country": null }, { "creditor.pqCountry": "BEL" }] } // 618769 payments
            
            ]// end or
        }
        
        ] // end and
    
    
})
