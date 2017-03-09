import {Injectable, OnInit} from '@angular/core';
import {Pregnancy} from "./pregnancy";


@Injectable()
export class PregnancyRepository implements OnInit {
  futureDb: Promise<IDBDatabase>;

  constructor() {
    this.ngOnInit();
  }

  ngOnInit(): void {
    let self = this;
    const dbName = "ProgLevDB";
    let request = indexedDB.open(dbName, 1);
    request.onupgradeneeded = function (event) {
      let db: IDBDatabase = request.result;
      db.createObjectStore("pregnancies", {keyPath: "id", autoIncrement: true});
    };
    this.futureDb = new Promise(function (resolve, reject) {
      request.onsuccess = function (event) {
        resolve(request.result);
      }
      request.onerror = function (event) {
        reject(event);
      }
    });


  }

  getAll(): Promise<Pregnancy[]> {
    return this.openObjectStore()
      .then(objectStore => new Promise(function (resolve, reject) {
        let pregnancies: Pregnancy[] = [];
        let openCursorRequest = objectStore.openCursor();
        openCursorRequest.onsuccess = function (event) {
          let cursor: IDBCursorWithValue = openCursorRequest.result;
          if (cursor) {
            pregnancies.push(cursor.value);
            cursor.continue();
          } else {
            resolve(pregnancies);
          }
        };
        openCursorRequest.onerror = reject;
      })
    );
  }

  getById(id: number): Promise<Pregnancy> {
    return this.openObjectStore()
      .then(objectStore => new Promise(function (resolve, reject) {
        let request = objectStore.get(id);
        request.onsuccess = function (event) {
          let pregnancy = request.result;
          resolve(pregnancy);
        };
        request.onerror = reject;
      }));
  }

  addPregnancy(pregnancy: Pregnancy): Promise<Pregnancy> {
    return this.openObjectStore("readwrite")
      .then(objectStore => new Promise(function (resolve, reject) {
        let objectStoreRequest = objectStore.add(pregnancy);
        objectStoreRequest.onsuccess = function (event: any) {
          pregnancy.id = objectStoreRequest.result;
          resolve(pregnancy);
        };
        objectStoreRequest.onerror = reject;
      }));

  }

  savePregnancy(pregnancy: Pregnancy): Promise<Pregnancy> {
    return this.openObjectStore("readwrite")
      .then(objectStore => new Promise(function (resolve, reject) {
        let objectStoreRequest = objectStore.put(pregnancy);
        objectStoreRequest.onsuccess = function (event) {
          if (!pregnancy.id){
            pregnancy.id = objectStoreRequest.result;
          }
          resolve(pregnancy);
        };
        objectStoreRequest.onerror = reject;
      }));

  }

  private openObjectStore(mode?: string): Promise<IDBObjectStore> {
    if (!mode){
      mode = "readonly";
    }
    return this.futureDb.then(db => Promise.resolve(db.transaction(["pregnancies"], mode).objectStore("pregnancies")));
  }
}
