<?php

class Raids extends BaseRaids
{
  public function getZone(){
    return $this->getBosses()->getZones();
  }
  
  public function getBoss(){
    return $this->getBosses();
  }
  
  public function getRoute(){
    return 'raid/show?id='.$this->id;
  }
  
  public function mdyDate(){
    return date('m/d/y', strtotime($this->date));
  }
  
  public function echoMdyLink(){
    echo link_to($this->mdyDate(), $this->getRoute());
  }
  
  public function echoNoteLink(){
    echo link_to($this->note, $this->getRoute());
  }
  
  public function __toString(){
    return $this->note.' ('.$this->getBoss()->getName().')';
  }
  
  public function save(PropelPDO $con = null){
    return parent::save($con);
  }
}
