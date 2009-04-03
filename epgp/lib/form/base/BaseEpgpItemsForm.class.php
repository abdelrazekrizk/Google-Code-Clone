<?php

/**
 * EpgpItems form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseEpgpItemsForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'          => new sfWidgetFormInputHidden(),
      'name'        => new sfWidgetFormInput(),
      'awarded_to'  => new sfWidgetFormPropelChoice(array('model' => 'EpgpRoster', 'add_empty' => false)),
      'awarded_at'  => new sfWidgetFormPropelChoice(array('model' => 'EpgpRaids', 'add_empty' => false)),
      'itemid'      => new sfWidgetFormInput(),
      'baseval'     => new sfWidgetFormInput(),
      'inflatedval' => new sfWidgetFormInput(),
    ));

    $this->setValidators(array(
      'id'          => new sfValidatorPropelChoice(array('model' => 'EpgpItems', 'column' => 'id', 'required' => false)),
      'name'        => new sfValidatorString(array('max_length' => 255)),
      'awarded_to'  => new sfValidatorPropelChoice(array('model' => 'EpgpRoster', 'column' => 'id')),
      'awarded_at'  => new sfValidatorPropelChoice(array('model' => 'EpgpRaids', 'column' => 'id')),
      'itemid'      => new sfValidatorInteger(),
      'baseval'     => new sfValidatorNumber(),
      'inflatedval' => new sfValidatorNumber(),
    ));

    $this->widgetSchema->setNameFormat('epgp_items[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'EpgpItems';
  }


}
