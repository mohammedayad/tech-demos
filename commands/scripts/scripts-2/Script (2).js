// db["unregisteredBankUsers"].find({iban: { $regex: "^BE" }}).count()

const BATCH_SIZE = 50000;     // how many docs per batch
const MAX_BATCHES = 20;      // hard limit on iterations
const startTime = Date.now();
for (let i = 0; i < MAX_BATCHES; i++) {
  print(`Batch ${i + 1} starting...`);
  const batch = db.unregisteredBankUsers
    .find({iban: { $regex: "^BE" }})
    .limit(BATCH_SIZE)
    .toArray();
  const count = batch.length;
  if (count === 0) {
    print("No more documents to delete. Stopping early.");
    break;
  }
  const ids = batch.map(doc => doc._id);
  const result = db.unregisteredBankUsers.deleteMany({
    _id: { $in: ids }
  });
  print(`Deleted ${result.deletedCount} documents in batch ${i + 1}`);
}
  const endTime = Date.now();
  const duration = endTime - startTime;
print(`Batch deletion finished in ${duration} ms.`);
