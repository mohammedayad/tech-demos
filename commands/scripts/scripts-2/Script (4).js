//db.getCollection("transactions").find({})
db.transactions.createIndex({ "creditor.contractualOwner": 1},{ name: "bpc_cleanup_idx" }  );
db.transactions.dropIndex("bpc_data_cleanup_idx");
