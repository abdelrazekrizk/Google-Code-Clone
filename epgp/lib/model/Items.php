<?php

class Items extends BaseItems
{
  public function getAwardeeName(){
    return $this->getRoster()->getName();
  }
  
  public function getRaid(){
    return $this->getRaids();
  }
  
  public function getAwardee(){
    return $this->getRoster();
  }
  
  public function getBoss(){
    return $this->getRaids()->getBosses();
  }
  
  public function getZone(){
    return $this->getRaids()->getBosses()->getZones();
  }
  
  public function getRoute(){
    return 'items/show?name='.$this->name;
  }
  
  public function echoLink(){
    echo link_to($this->name, $this->getRoute());
  }
  
  public function save(PropelPDO $con = null){
    $this->setInflatedVal($this->getBaseVal());
    return parent::save($con);
  }
}
