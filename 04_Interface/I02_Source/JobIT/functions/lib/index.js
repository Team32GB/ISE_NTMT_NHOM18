"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require("firebase-functions");
const admin = require("firebase-admin");
const algoliasearch = require("algoliasearch");
admin.initializeApp();
const algolia = algoliasearch('OYGFHT3AFC', '5d4c2ba1711e8b20d3754bc717222edc');
const index = algolia.initIndex('tinTuyenDungs');
exports.syncAlgoliaWithFirebase = functions.database.ref('/tinTuyenDungs/{idCompany}/{idTin}')
    .onWrite((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idCompany = context.params.idCompany;
    const idTin = context.params.idTin;
    const timeJob = snapshot.after.child('nameJob').val();
    console.log('companyId: ' + idCompany + ' ' + idTin + ' ' + timeJob);
    const records = [];
    // get the key and data from the snapshot
    const childKey = idTin;
    const childData = snapshot.after.val();
    // We set the Algolia objectID as the Firebase .key
    childData.objectID = childKey;
    // Add object for indexing
    records.push(childData);
    // Add or update new objects
    return index
        .saveObjects(records)
        .then(() => {
        console.log('Contacts imported into Algolia');
    });
    // .catch(error => {
    //  console.error('Error when importing contact into Algolia', error)
    //  process.exit(1)
    // })
}));
exports.thongBaoUngVienApply = functions.database.ref('/choDuyets/{companyId}/{idJob}/{idUngVien}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const companyId = context.params.companyId;
    const idJob = context.params.idJob;
    const idUngVien = context.params.idUngVien;
    const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value');
    const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value');
    const results = yield Promise.all([getNameUngVien, getTinTuyenDung]);
    const nameSnapshot = results[0];
    const tinTuyenDungSnapshot = results[1];
    const nameUngVien = nameSnapshot.val();
    const tinTuyenDung = tinTuyenDungSnapshot.val();
    const timeJob = snapshot.child('timeApplied').val();
    console.log('companyId: ' + companyId + ',jobId: ' + idJob + ",idungvien: " + idUngVien + ",time " + timeJob);
    console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung);
    let tinTuyenDungStr = tinTuyenDung;
    const payload = {
        notification: {
            title: 'Có ứng viên apply',
            body: 'Ứng viên ' + nameUngVien + ' đã apply ' + tinTuyenDung + ' của bạn',
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoUngVienApply',
            idCompany: companyId + '',
            idJob: idJob + '',
            nameJob: tinTuyenDungStr,
            timeJob: timeJob + ''
        }
    };
    return admin.database().ref('/fcm_tokens/' + companyId + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoMoiPhongVan = functions.database.ref('/choPhongVanNTVs/{idUngVien}/{companyId}/{idJob}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const companyId = context.params.companyId;
    const idJob = context.params.idJob;
    const idUngVien = context.params.idUngVien;
    const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value');
    const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value');
    const getNameCompany = admin.database().ref('/companys/' + companyId + '/name').once('value');
    const results = yield Promise.all([getNameUngVien, getTinTuyenDung, getNameCompany]);
    const nameSnapshot = results[0];
    const tinTuyenDungSnapshot = results[1];
    const nameCompanySnapshot = results[2];
    const nameUngVien = nameSnapshot.val();
    const tinTuyenDung = tinTuyenDungSnapshot.val();
    const timeJob = snapshot.child('timeApplied').val();
    const nameCompany = nameCompanySnapshot.val();
    console.log('companyId: ' + companyId + 'name: ' + nameCompany + ',jobId: ' + idJob + ",idungvien: " + idUngVien + ",time " + timeJob);
    console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung);
    let tinTuyenDungStr = tinTuyenDung;
    let nameCpStr = nameCompany;
    const payload = {
        notification: {
            title: 'Bạn được mời phỏng vấn',
            body: 'Công ty ' + nameCompany + ' đã mời bạn phỏng vấn công việc ' + tinTuyenDung,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoMoiPhongVan',
            idCompany: companyId + '',
            idJob: idJob + '',
            nameJob: tinTuyenDungStr,
            timeJob: timeJob + '',
            nameCompany: nameCpStr + ''
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUngVien + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoMoiLam = functions.database.ref('/moiLamNTVs/{idUngVien}/{companyId}/{idJob}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const companyId = context.params.companyId;
    const idJob = context.params.idJob;
    const idUngVien = context.params.idUngVien;
    const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value');
    const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value');
    const getNameCompany = admin.database().ref('/companys/' + companyId + '/name').once('value');
    const results = yield Promise.all([getNameUngVien, getTinTuyenDung, getNameCompany]);
    const nameSnapshot = results[0];
    const tinTuyenDungSnapshot = results[1];
    const nameCompanySnapshot = results[2];
    const nameUngVien = nameSnapshot.val();
    const tinTuyenDung = tinTuyenDungSnapshot.val();
    const timeJob = snapshot.child('timeApplied').val();
    const nameCompany = nameCompanySnapshot.val();
    console.log('companyId: ' + companyId + 'name: ' + nameCompany + ',jobId: ' + idJob + ",idungvien: " + idUngVien + ",time " + timeJob);
    console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung);
    let tinTuyenDungStr = tinTuyenDung;
    let nameCpStr = nameCompany;
    const payload = {
        notification: {
            title: 'Bạn được mời làm',
            body: 'Công ty ' + nameCompany + ' đã mời bạn làm công việc ' + tinTuyenDung,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoMoiLam',
            idCompany: companyId + '',
            idJob: idJob + '',
            nameJob: tinTuyenDungStr,
            timeJob: timeJob + '',
            nameCompany: nameCpStr + ''
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUngVien + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.loaiPhongVan = functions.database.ref('/loaiPhongVans/{idUngVien}/{companyId}/{idJob}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const companyId = context.params.companyId;
    const idJob = context.params.idJob;
    const idUngVien = context.params.idUngVien;
    const getNameUngVien = admin.database().ref('/jobseekers/' + idUngVien + '/email').once('value');
    const getTinTuyenDung = admin.database().ref('/tinTuyenDungs/' + companyId + '/' + idJob + '/nameJob').once('value');
    const getNameCompany = admin.database().ref('/companys/' + companyId + '/name').once('value');
    const results = yield Promise.all([getNameUngVien, getTinTuyenDung, getNameCompany]);
    const nameSnapshot = results[0];
    const tinTuyenDungSnapshot = results[1];
    const nameCompanySnapshot = results[2];
    const nameUngVien = nameSnapshot.val();
    const tinTuyenDung = tinTuyenDungSnapshot.val();
    const timeJob = snapshot.child('timeApplied').val();
    const nameCompany = nameCompanySnapshot.val();
    console.log('companyId: ' + companyId + 'name: ' + nameCompany + ',jobId: ' + idJob + ",idungvien: " + idUngVien + ",time " + timeJob);
    console.log('name: ' + nameUngVien + ' tin: ' + tinTuyenDung);
    let tinTuyenDungStr = tinTuyenDung;
    let nameCpStr = nameCompany;
    const payload = {
        notification: {
            title: 'Bạn bị loại khi duyệt CV',
            body: 'Công ty ' + nameCompany + ' đã loại CV của bạn trong công việc ' + tinTuyenDung,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'loaiPhongVan',
            idCompany: companyId + '',
            idJob: idJob + '',
            nameJob: tinTuyenDungStr,
            timeJob: timeJob + '',
            nameCompany: nameCpStr + ''
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUngVien + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoPheDuyetNhaTuyenDung = functions.database.ref('/companys/{companyId}/approvalMode')
    .onUpdate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const companyId = context.params.companyId;
    const approvalMode = admin.database().ref('/companys/' + companyId + '/approvalMode').once('value');
    const results = yield Promise.all([approvalMode]);
    const approvalModeSnapshot = results[0];
    const isApproval = parseInt(approvalModeSnapshot.val());
    let payload;
    console.log('companyId: ' + companyId + ', Approval mode: ' + isApproval);
    if (isApproval === 1) {
        payload = {
            notification: {
                title: 'Phê duyệt hồ sơ thành công',
                body: 'Hồ sơ của bạn trên Job IT đã được thành công',
                badge: '1',
                sound: 'default'
            }
        };
    }
    else {
        payload = {
            notification: {
                title: 'Phê duyệt hồ sơ thất bại',
                body: 'Hồ sơ trên Job IT của bạn không được duyệt. Bạn hãy kiểm tra lại.',
                badge: '1',
                sound: 'default'
            }
        };
    }
    return admin.database().ref('/fcm_tokens/' + companyId + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoCanhCaoJobseeker = functions.database.ref('/reports/jobseekers/{idUser}/{idReport}/isWarned')
    .onUpdate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idUser = context.params.idUser;
    const idReport = context.params.idReport;
    const adminCommentData = admin.database().ref('/reports/jobseekers/' + idUser + '/' + idReport + '/adminComment').once('value');
    console.log('jobSeekerId: ' + idUser + ', idReport ' + idReport, ', adminComment: ' + adminCommentData);
    const results = yield Promise.all([adminCommentData]);
    const dataSnapshot = results[0];
    const adminComment = dataSnapshot.val();
    const payload = {
        notification: {
            title: 'Cảnh cáo',
            body: adminComment,
            badge: '1',
            sound: 'default'
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUser + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoCanhCaoRecruiter = functions.database.ref('/reports/recruiters/{idUser}/{idReport}/isWarned')
    .onUpdate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idUser = context.params.idUser;
    const idReport = context.params.idReport;
    const adminCommentData = admin.database().ref('/reports/recruiters/' + idUser + '/' + idReport + '/adminComment').once('value');
    console.log('recruitersID: ' + idUser + ', idReport ' + idReport, ', adminComment: ' + adminCommentData);
    const results = yield Promise.all([adminCommentData]);
    const dataSnapshot = results[0];
    const adminComment = dataSnapshot.val();
    const payload = {
        notification: {
            title: 'Cảnh cáo',
            body: adminComment,
            badge: '1',
            sound: 'default'
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUser + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoAdminToCaoRecruiterMoi = functions.database.ref('/reports/recruiters/{idUser}/{idReport}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idUser = context.params.idUser;
    const idReport = context.params.idReport;
    const name = admin.database().ref('/companys/' + idUser + '/name').once('value');
    const dateSend = admin.database().ref('/reportWaitingAdminApproval/recruiters/' + idReport + '/dateSendReport').once('value');
    const results = yield Promise.all([name, dateSend]);
    const dataSnapshot = results[0];
    const nameCompany = dataSnapshot.val();
    const dataDate = results[1];
    const date = dataDate.val();
    console.log('id user: ' + idUser + ', name user: ' + name, ', date: ' + date);
    const payload = {
        notification: {
            title: 'Tố cáo mới',
            body: 'Đơn vị bị tố cáo: ' + nameCompany,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoAdminToCaoRecruiterMoi',
            idReport: idReport + '',
            idUser: idUser + '',
            date: date + '',
            idAccused: idUser + '',
        }
    };
    return admin.database().ref('/fcm_tokens/Xf48VViAaoMAcNvgWvveiDepiq02' + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoAdminToCaoJobSeekerMoi = functions.database.ref('/reports/jobseekers/{idUser}/{idReport}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idUser = context.params.idUser;
    const idReport = context.params.idReport;
    const name = admin.database().ref('/jobseekers/' + idUser + '/name').once('value');
    const dateSend = admin.database().ref('/reportWaitingAdminApproval/jobseekers/' + idReport + '/dateSendReport').once('value');
    const results = yield Promise.all([name, dateSend]);
    const dataSnapshot = results[0];
    const nameJobseeker = dataSnapshot.val();
    const dataDate = results[1];
    const date = dataDate.val();
    console.log('id user: ' + idUser + ', name user: ' + name + ', date: ' + date);
    const payload = {
        notification: {
            title: 'Tố cáo mới',
            body: 'Người bị tố cáo: ' + nameJobseeker,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoAdminToCaoJobSeekerMoi',
            idReport: idReport + '',
            date: date + '',
            idAccused: idUser + ''
        }
    };
    return admin.database().ref('/fcm_tokens/Xf48VViAaoMAcNvgWvveiDepiq02/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoAdminHoSoMoi = functions.database.ref('/companysWaitingAdminApproval/{idCompany}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idCompany = context.params.idCompany;
    const name = admin.database().ref('/companys/' + idCompany + '/name').once('value');
    const dateSend = admin.database().ref('/companysWaitingAdminApproval/' + idCompany + '/dateSendApproval').once('value');
    const results = yield Promise.all([name, dateSend]);
    const dataSnapshot = results[0];
    const nameCompany = dataSnapshot.val();
    const dataDate = results[1];
    const date = dataDate.val();
    console.log('id company: ' + idCompany + ', name company: ' + name, ', date: ' + date);
    const payload = {
        notification: {
            title: 'Hồ sơ cần duyệt mới',
            body: 'Tên công ty: ' + nameCompany,
            badge: '1',
            sound: 'default'
        },
        data: {
            type: 'thongBaoAdminHoSoMoi',
            idCompany: idCompany + '',
            date: date + ''
        }
    };
    return admin.database().ref('/fcm_tokens/Xf48VViAaoMAcNvgWvveiDepiq02/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
exports.thongBaoBiKhoaTaiKhoan = functions.database.ref('/unActiveUsers/{idUser}')
    .onCreate((snapshot, context) => __awaiter(this, void 0, void 0, function* () {
    const idUser = context.params.idUser;
    console.log('id user: ' + idUser);
    const payload = {
        notification: {
            title: 'Tài khoản của bạn đã bị khóa',
            body: 'Bạn bị tố cáo nhiều lần nên tài khoản đã bị admin khóa',
            badge: '1',
            sound: 'default'
        }
    };
    return admin.database().ref('/fcm_tokens/' + idUser + '/token').once('value')
        .then(fcm_token => {
        console.log('token available : ' + fcm_token.val());
        return admin.messaging().sendToDevice(fcm_token.val(), payload);
    });
}));
//# sourceMappingURL=index.js.map