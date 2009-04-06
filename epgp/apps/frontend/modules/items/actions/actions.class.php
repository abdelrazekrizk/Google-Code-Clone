<?php

/**
 * items actions.
 *
 * @package    epgp
 * @subpackage items
 * @author     Your name here
 * @version    SVN: $Id: actions.class.php 12479 2008-10-31 10:54:40Z fabien $
 */
class itemsActions extends sfActions
{
 /**
  * Executes index action
  *
  * @param sfRequest $request A request object
  */
  public function executeIndex(sfWebRequest $request)
  {
    // todo: show all items
  }
  
  public function executeShow(sfWebRequest $request){
    $this->itemname = $request->getParameter('name');
    
    $c = new Criteria();
    $c->add(ItemsPeer::NAME, $this->itemname);
    
    $this->itemid = ItemsPeer::doSelectOne($c);
    
    $this->forward404Unless($this->itemid);
    
    $this->itemid = $this->itemid->getItemId();
    
    $this->awards = ItemsPeer::doSelectJoinAll($c);
  }
}
