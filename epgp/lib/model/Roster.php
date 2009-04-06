<?php

class Roster extends BaseRoster
{
  public function echoLink(){
    echo link_to($this, $this->getRoute());
  }
  
  public function getRoute(){
    return 'character/show?name='.$this->name;
  }
  
  public function getClass(){
    return $this->getClasses()->getName();
  }
  
  public function getRace(){
    return $this->getRaces()->getName();
  }
  
  public function __toString(){
    return $this->name;
  }
  
  public function getJoinedMdy(){
    return date('m/d/y', strtotime($this->getJoinedOn()));
  }
  
  public function getIsActiveStr(){
    return ($this->getIsActive() == 1)?'Yes':'No';
  }
  
  public function __construct(){
    $this->setJoinedOn(strtotime('today'));
    parent::__construct();
  }
  
  public function save(PropelPDO $con = null){
    parent::save($con);
  }
}
