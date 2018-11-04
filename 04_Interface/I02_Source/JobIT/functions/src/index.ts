import * as functions from 'firebase-functions'
import * as admin from 'firebase-admin'
admin.initializeApp()
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.thongBaoUngVienApply = functions.database.ref('/choDuyets/{companyId}/{idJob}')
    .onCreate(async(snapshot, context) => {
     
        const companyId = context.params.companyId
        const idJob = context.params.idJob
        //const jobId = snapshot.key
        const idUngVien = snapshot.child("idUngVien").val()
       // const id1 = snapshot.ref.child("idUngVien").once('value')
//const id2 = snapshot.child("idUngVien").key

      //  const ungVienId = context.params.idUngvien
       //const keyUngVien = snapshot.child("idUngVien").key
        //const companyId = snapshot.ref.parent.key
        
        //const nameUngVien : string
        console.log('companyId: ' + companyId + ',jobId: ' + idJob + ",idungvien: " + idUngVien)
        const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value')
        // .then(nameUngVienSn => {
        //     nameUngVien = nameUngVienSn.val()
        // });
        const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value')

    
        
        const results = await Promise.all([getNameUngVien,getTinTuyenDung])
        const nameSnapshot = results[0]
        const tinTuyenDungSnapshot = results[1]

        const nameUngVien = nameSnapshot.val()
        const tinTuyenDung = tinTuyenDungSnapshot.val()

        console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung)

        const payload = {
            notification:{
                title: 'Co ung vien apply',
                body: 'ung vien ' + nameUngVien + ' đã apply ' + tinTuyenDung + ' của bạn', 
                badge: '1',
                sound: 'default'
            }
        }

        // return admin.database().ref('/fcm_tokens/' + companyId + '/token').once('value')
        // .then(fcm_token => {
        //         console.log('token available : ' + fcm_token.val())
        //         return admin.messaging().sendToDevice(fcm_token.val(),payload)
        // })
        return admin.database().ref('/fcm_tokens/' + companyId + '/token').once('value')
        .then(fcm_token => {
                console.log('token available : ' + fcm_token.val())
               return admin.messaging().sendToDevice(fcm_token.val(),payload)
        })


        // return Promise.all
    })

    exports.thongBaoChapNhanUngVien = functions.database.ref('/choDuyets/{companyId}/{idJob}')
    .onCreate(async(snapshot, context) => {
     
        const companyId = context.params.companyId
        const idJob = context.params.idJob

        const idUngVien = snapshot.child("idUngVien").val()

        
        //const nameUngVien : string
        console.log('companyId: ' + companyId + ',jobId: ' + idJob + ",idungvien: " + idUngVien)
        const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value')

        const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value')

    
        
        const results = await Promise.all([getNameUngVien,getTinTuyenDung])
        const nameSnapshot = results[0]
        const tinTuyenDungSnapshot = results[1]

        const nameUngVien = nameSnapshot.val()
        const tinTuyenDung = tinTuyenDungSnapshot.val()

        console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung)

        const payload = {
            notification:{
                title: 'Co ung vien apply',
                body: 'ung vien ' + nameUngVien + ' đã apply ' + tinTuyenDung + ' của bạn', 
                badge: '1',
                sound: 'default'
            }
        }

        return admin.database().ref('/fcm_tokens/' + companyId + '/token').once('value')
        .then(fcm_token => {
                console.log('token available : ' + fcm_token.val())
               return admin.messaging().sendToDevice(fcm_token.val(),payload)
        })


        // return Promise.all
    })