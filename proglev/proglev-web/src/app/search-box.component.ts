import {Component, Output, EventEmitter, OnInit} from '@angular/core';


@Component({
    selector: 'search-box',
    template: `
        <div class="input-group">        
            <input #input type="text" class="form-control" (input)="emit(input.value)" placeholder="Wpisz imię, nazwisko lub e-mail aby wyszukać">
            <span class="input-group-addon"><i class="glyphicon glyphicon-search"></i></span>           
        </div>
    `
})
export class SearchBox implements OnInit{
    @Output() update = new EventEmitter();

    ngOnInit(){
        this.update.emit('');
    }

    emit(value){
        this.update.emit(value);
    }
}