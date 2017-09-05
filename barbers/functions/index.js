const functions = require('firebase-functions');

const admin = require('firebase-admin');
const uuid = require('uuid');
admin.initializeApp(functions.config().firebase);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.addMockData = functions.https.onRequest((req, res) =>{

    const shop = {
        id: uuid.v4(),
        name: "Good barber",
        phone: "+91-8894121222",
        address: "46, Sector 56, Gurgaon, Haryana",
        seats: 1,
        likes: 34,
        sevices_m: [
            {
                name: "Hair Cut",
                time_mins: 20,
                price: 100
            },
            {
                name: "Shave",
                time_mins: 15,
                price: 50
            },
            {
                name: "Massage",
                time_mins: 30,
                price: 150
            }
        ],
        services_f: [
            {
                name: "Pedicure",
                time_mins: 50,
                price: 250
            },
            {
                name: "Hair Cut",
                time_mins: 45,
                price: 150
            },
            {
                name: "Manicure",
                time_mins: 45,
                price: 200
            }
        ],
        reviews:[
            {
                id: uuid.v4(),
                text: "Good for ladies, but not so much for kids",
                timestamp: admin.database.ServerValue.TIMESTAMP
            },
            {
                id: uuid.v4(),
                text: "Massaage was not that great",
                timestamp: admin.database.ServerValue.TIMESTAMP
            }
        ],
        lat: "123123",
        loc: "234343",
        bookings: [
            {
                bookingId: '12312123123177',
                date: '05-09-2017',
                startTime: '1000',
                endTime: '1200'
            }
        ]
    }

    const booking = {
        bookingId: '12312123123177',
        userId: 'temp_user',
        startTime: '1000',
        endTime: '1200',
        shopId: '67a1f276-5981-47ec-8234-56218811ea98'
    }

    admin.database().ref('/shops').push(shop)
        .then(snap=>{
            admin.database().ref('/bookings').push(booking)
            .then(snap=>{
                res.send(200, {status: false, message: "failed"});
            }).catch(err=>{
                res.send(200, {status: true, message: "success"});
            });
        }).catch(err=>{
            res.send(200, {status: false, message:"failed"});
        })
});
