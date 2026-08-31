//db.getCollection("camtFile").find({})

//db.camtFile.drop()
//db.shedLock.drop()

let collections1 = db.getCollectionNames()
print("\nChecking in camtProcessingServiceEXT:")
print("camtFile: " + (collections1.includes("camtFile") ? "EXISTS" : "REMOVED"))
print("shedLock: " + (collections1.includes("shedLock") ? "EXISTS" : "REMOVED"))