<?php

class Bosses extends BaseBosses
{
  public function getRoute(){
    return 'boss/show?name='.$this->name;
  }
  
  public function getZone(){
    return $this->getZones();
  }
  
  public function getKillCount(){
    $c = new Criteria();
    $c->add(RaidsPeer::BOSS, $this->getId());
    return RaidsPeer::doCount($c);
  }
  
  public function __toString(){
    return $this->name;
  }
  
  public function echoLink(){
    echo link_to($this, $this->getRoute());
  }
}
