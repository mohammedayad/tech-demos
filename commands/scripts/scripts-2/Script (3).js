//db.getCollection("unregisteredUser").find({
//    unregisteredDate: { $exists: false },
//    type: "merchant"
//})

//db["unregisteredUser"].deleteMany({
//  unregisteredDate: { $exists: false },
//  type: "merchant"
//});


db["unregisteredUser"].countDocuments({
  unregisteredDate: { $exists: false },
  type: "merchant"
})
// should return 0